package org.gside.jersy.sample;
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import org.gside.jersy.sample.exception.GenericExceptionMapper;
import org.gside.jersy.sample.exception.IlegalFileExceptionMapper;
import org.gside.jersy.sample.exception.UnAuthorizedExceptionMapper;
import org.gside.jersy.sample.exception.WebApplicationExceptionMapper;
import org.gside.jersy.sample.rest.MovielenFileResource;
import org.gside.jersy.sample.rest.TokenResource;

/**
 * アプリケーション全体の設定をするクラス
 * 
 * @author matsuba
 *
 */
@ApplicationPath("/")
public class Application extends ResourceConfig {

	/**
	 * コンストラクタ。
	 * アプリケーションへクラスの設定を行う。 
	 */
	public Application() {
		super(MovielenFileResource.class,
				TokenResource.class,
				MultiPartFeature.class, ObjectMapperProvider.class, JacksonFeature.class, UnAuthorizedExceptionMapper.class, IlegalFileExceptionMapper.class, WebApplicationExceptionMapper.class, GenericExceptionMapper.class);
		
	}
}
