
package kittenbbq.discordbot;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StrawPoll {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("multi")
    @Expose
    private Boolean multi;
    @SerializedName("options")
    @Expose
    private List<String> options = null;
    @SerializedName("votes")
    @Expose
    private List<Long> votes = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getMulti() {
        return multi;
    }

    public void setMulti(Boolean multi) {
        this.multi = multi;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<Long> getVotes() {
        return votes;
    }

    public void setVotes(List<Long> votes) {
        this.votes = votes;
    }

}
