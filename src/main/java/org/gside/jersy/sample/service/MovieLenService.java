package org.gside.jersy.sample.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.gside.jersy.sample.exception.IllegalInputException;
import org.gside.jersy.sample.json.JsonConverter;
import org.gside.jersy.sample.model.MovielenFileModel;
import org.slf4j.Logger;

import cm.assignment.config.Config;
import org.gside.jersy.sample.entity.MovielenFile;
import org.gside.jersy.sample.entity.Token;
import org.gside.jersy.sample.exception.UnAuthorizedException;
import org.gside.jersy.sample.json.Rating;
import org.gside.jersy.sample.model.Status;
import org.gside.jersy.sample.model.TokenModel;

@ApplicationScoped
public class MovieLenService {

	@Inject
	private MovielenFileModel filesModel;
	@Inject
	private TokenModel tokenModel;
	@Inject
	private Logger logger;
	@Inject @Config("base.save.dir")
	private String baseSaveDir;
	@Inject @Config("base.url")
	private String baseUrl;
	@Inject 
	private Status status;

	private static final String RATING_CSV_FILE = "ratings.csv";
	private static final String RATING_JSON_FILE = "ratings.json";
	private static final String RATING_JSON_ZIP_FILE = "ratings.zip";
	private static final String TMP_FILE_SUFFIX = ".zip";
	private static final String TMP_FILE_PREFIX = "movielen";

	/**
	 * 指定されたハッシュ値の変換後のファイルURLを返す。
	 * 
	 * @param hash
	 * @return
	 */
	public Optional<MovielenFile> getFile(String hash) {
		if (hash == null || hash.isEmpty())
			return Optional.empty();
		logger.debug(hash);
		return filesModel.getByHash(hash);
	}

	/**
	 * ファイルをJsonline形式に変換して保存する。
	 * 
	 * @param tokenId
	 * @param input
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public String entry(String tokenId, InputStream input) throws IOException, NoSuchAlgorithmException {

		// Tokenをチェックする
		Optional<Token> token = tokenModel.get(tokenId);
		token.orElseThrow(() -> new UnAuthorizedException("token not found"));

		MovielenFile movielenFile = null;		
		try {
			if (status.isExists(tokenId)) {
				throw new UnAuthorizedException("File already uploaded");
			} else {
				status.putStatus(tokenId);
			}

			// 既にファイルが登録されていないかチェックする
			Optional<MovielenFile> file = getFile(token.get().getHash());
			if (file.isPresent()) {
				throw new IllegalInputException("your file already generated:  " + file.get().getUrl());
			}

			//　ファイルのhash値が正しいかチェックする
			String filePath = saveZipFile(input);
			String hash = getHash(filePath);
			if (! token.get().getHash().equals(hash)) {
				logger.info("hash:" + hash + " generate hash:" + token.get().getHash());
				throw new UnAuthorizedException("hash doesn't match");
			}


			// 生成するファイルを登録する。
			movielenFile = filesModel.save(hash, baseUrl + "/" + hash + "/" + RATING_JSON_ZIP_FILE);		 

			try (ZipInputStream zis = new ZipInputStream(new FileInputStream(new File(filePath)));
					InputStreamReader inputStreamReader = new InputStreamReader(zis);
					BufferedReader br = new BufferedReader(inputStreamReader, 8192 * 100)) {

				ZipEntry ze = zis.getNextEntry();
				JsonConverter<Rating> jsonConverter = new JsonConverter<>(Rating.class, Rating.JSON_ELEMENT);

				boolean ratingExists = false;
				while(ze!=null && zis.available() != 0){
					logger.debug(ze.getName());

					if (ze.getName().endsWith(RATING_CSV_FILE)) {

						File saveDir = new File(baseSaveDir + File.separator + hash);
						saveDir.mkdirs();
						String zipFilePath = baseSaveDir + File.separator + hash + File.separator + RATING_JSON_ZIP_FILE;
						logger.debug("Json Generation Start:" + zipFilePath);

						String line = br.readLine();
						if (!line.equals(Rating.CSV_HEADER)) {
							throw new IllegalInputException("header not found."); 
						}

						ratingExists = true;
						try(ZipOutputStream outputStream = new ZipOutputStream(
								new BufferedOutputStream(new FileOutputStream(zipFilePath)))){

							final ZipEntry entry = new ZipEntry(RATING_JSON_FILE);
							outputStream.putNextEntry(entry);

							while ((line = br.readLine()) != null) {
								byte[] json = jsonConverter.csvToJson(line).getBytes();
								outputStream.write(json);
								outputStream.write("\n".getBytes());
							}
						}

					}
					ze = zis.getNextEntry();
				}
				if (! ratingExists) {
					throw new IllegalInputException("raitings.csv is not exists.");
				}
				logger.debug("Json Generation Done:");
			}
		} finally {
			status.deleteStatus(tokenId);
		}

		return movielenFile.getUrl();

	}

	private String getHash(String filePath) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance("MD5");

		StringBuilder sb = new StringBuilder();
		try (InputStream is = Files.newInputStream(Paths.get(filePath));
				DigestInputStream dis = new DigestInputStream(is, md))	{

			// ファイルの読み込み
			while(dis.read() != -1) { }

			// ハッシュ値の計算
			byte[] digest = md.digest();

			for(int i=0; i< digest.length ;i++) {
				sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
			}
		} 

		return sb.toString();
	}

	private String saveZipFile(InputStream input) throws IOException {

		File tempFile = null;

		tempFile = File.createTempFile(TMP_FILE_PREFIX, TMP_FILE_SUFFIX);			 
		tempFile.deleteOnExit();
		try (OutputStream outStream = new FileOutputStream(tempFile)) {
			int bytesRead;
			byte[] buffer = new byte[8 * 1024];
			while ((bytesRead = input.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
		}
		return tempFile.getPath();
	}
	public static void decode(File file) throws IOException {
		try (ZipFile zipFile = new ZipFile(file, Charset.forName("MS932"))) {
			zipFile.stream().filter(entry -> !entry.isDirectory()).forEach(entry -> {
				try (InputStream is = zipFile.getInputStream(entry)) {
					byte[] buf = new byte[1024];
					for (;;) {
						int len = is.read(buf);
						if (len < 0) {
							break;
						}
						// buf繧剃ｽｿ縺｣縺ｦ蜃ｦ逅�
					}
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			});
		}
	}    
}
