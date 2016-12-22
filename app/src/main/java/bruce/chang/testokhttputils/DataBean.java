package bruce.chang.testokhttputils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator
 * Date:2016/12/21
 * Time:22:39
 * Author:BruceChang
 * Function:
 */

public class DataBean implements Serializable {


    /**
     * id : 63799
     * movieName : 《敦刻尔克》剧场版预告
     * coverImg : http://img5.mtime.cn/mg/2016/12/15/174641.37616965.jpg
     * movieId : 230741
     * url : http://vfx.mtime.cn/Video/2016/12/14/mp4/161214234616222245_480.mp4
     * hightUrl : http://vfx.mtime.cn/Video/2016/12/14/mp4/161214234616222245.mp4
     * videoTitle : 敦刻尔克 剧场版预告片
     * videoLength : 129
     * rating : -1
     * type : ["动作","剧情","历史","惊悚","战争"]
     * summary : 军民协作“大撤退”
     */

    private List<ItemData> trailers;

    public List<ItemData> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<ItemData> trailers) {
        this.trailers = trailers;
    }

    public static class ItemData {
        private int id;
        private String movieName;
        private String coverImg;
        private int movieId;
        private String url;
        private String hightUrl;
        private String videoTitle;
        private int videoLength;
        private int rating;
        private String summary;
        private List<String> type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMovieName() {
            return movieName;
        }

        public void setMovieName(String movieName) {
            this.movieName = movieName;
        }

        public String getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(String coverImg) {
            this.coverImg = coverImg;
        }

        public int getMovieId() {
            return movieId;
        }

        public void setMovieId(int movieId) {
            this.movieId = movieId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getHightUrl() {
            return hightUrl;
        }

        public void setHightUrl(String hightUrl) {
            this.hightUrl = hightUrl;
        }

        public String getVideoTitle() {
            return videoTitle;
        }

        public void setVideoTitle(String videoTitle) {
            this.videoTitle = videoTitle;
        }

        public int getVideoLength() {
            return videoLength;
        }

        public void setVideoLength(int videoLength) {
            this.videoLength = videoLength;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public List<String> getType() {
            return type;
        }

        public void setType(List<String> type) {
            this.type = type;
        }
    }
}
