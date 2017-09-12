package org.gside.jersy.sample.json;

/**
 * rating.csvの構造を表すクラス。
 * 
 * @author matsuba
 *
 */
public class Rating {
	/** 項目の要素	 */
	public static String[] JSON_ELEMENT = {"userId", "movieId", "rating", "timestamp"};
	/** CSVのヘッダー */
	public static String CSV_HEADER = "userId,movieId,rating,timestamp";
	private String userId;
	private String movieId;
	private String rating;
	private String timestamp;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getMovieId() {
		return movieId;
	}
	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}