package cd.go.authentication.passwordfile.executor;

import cd.go.authentication.passwordfile.utils.Util;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GetPluginIconExecutorTest {

    @Test
    public void rendersIconInBase64() throws Exception {
        GoPluginApiResponse response = new GetPluginIconExecutor().execute();
        HashMap<String, String> hashMap = new Gson().fromJson(response.responseBody(), HashMap.class);
        assertThat(hashMap.size(), is(2));
        assertThat(hashMap.get("content_type"), is("image/png"));
        assertThat(Util.readResourceBytes("/gocd_72_72_icon.png"), is(Base64.decodeBase64(hashMap.get("data"))));
    }
}