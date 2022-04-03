package backend.iEcp.JSON;

import com.google.gson.annotations.SerializedName;

public final class JSONView extends JSONMain {
    @SerializedName(value = "requestId")
    private final int requestID;

    public JSONView(int requestID) {
        super();

        this.requestID = requestID;
    }
}
