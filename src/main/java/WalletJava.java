import com.google.gson.Gson;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import okhttp3.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class WalletJava {

    private final static String URL ="http://localhost:19001";

    private final static String USER = "admin1";
    private final static String PASS = "123";

    public static void main(String[] args) throws IOException {
//        System.out.println(getNewAddress("cccc"));
        System.out.println(listReceivedByAddress("2NB84S5tttA9df3y5ay983bpqkXZsfwo7gs"));
    }

    public static String getAuthorization(String user,String pass){
        String auth = String.format("%s:%s",user,pass);
        System.out.println(auth);
        return String.format("Basic %s", Base64.encode(auth.getBytes(StandardCharsets.UTF_8)));
    }

    public static String getAuthorization(){
        return getAuthorization(USER,PASS);
    }

    //获取新地址
    /**
     *
     * @param label//获取新地址
     */
    public static String getNewAddress(String label) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"method\":\"getnewaddress\",\"params\":[\""+label+"\"]\n}");
        Response response = client.newCall(builder(body)).execute();
        Gson gson = new Gson();
        Map<String,String> map = gson.fromJson(response.body().string(), Map.class);
        return map.get("result");
    }

    //转账
    public static void sendToAddress(String address, BigDecimal num,String remark,String personName) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"jsonrpc\": \"1.0\",\n    \"id\": \"curltest\",\n    \"method\": \"sendtoaddress\",\n    \"params\": [\n        \""+address+"\",\n        "+num+",\n        \""+remark+"\",\n        \""+personName+"\"\n    ]\n}");
        Response response = client.newCall(builder(body)).execute();
    }

    public static Request builder(RequestBody body){
        String authorization = getAuthorization();
        System.out.println(authorization);
        return new Request.Builder()
                .url(URL)
                .method("POST", body)
                .addHeader("Authorization", authorization)
                .addHeader("Content-Type", "application/json")
                .build();
    }

    //获取钱包余额
    public static Object getBalance() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"method\":\"getbalance\"\n}");
        Response response = client.newCall(builder(body)).execute();
        Gson gson = new Gson();
        Map<String,Object> map = gson.fromJson(response.body().string(), Map.class);
        return map.get("result");
    }

    //获取钱包余额
    public static Object getReceivedByAddress(String address) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"getreceivedbyaddress\",\"params\":[\""+address+"\"]}");
        Response response = client.newCall(builder(body)).execute();
        Gson gson = new Gson();
        Map<String,Object> map = gson.fromJson(response.body().string(), Map.class);
        return map.get("result");
    }

    class ResultItem{
        String address;
        BigDecimal amount;
    }
    class ResultObject {
        List<ResultItem> result;
        String error;
        String id;
    }
    public static Object listReceivedByAddress(String address) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"method\":\"listreceivedbyaddress\",\"params\":[6,false,false,\""+address+"\"]}");
        Response response = client.newCall(builder(body)).execute();
//        System.out.println(response.body().string());
        Gson gson = new Gson();
        ResultObject resultObject = gson.fromJson(response.body().string(), ResultObject.class);
        return resultObject.result;
    }
}
