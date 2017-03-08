package cd.go.authentication.passwordfile;

import com.thoughtworks.go.plugin.api.GoPluginIdentifier;

import java.util.Collections;

public interface Constants {
    // The type of this extension
    String EXTENSION_TYPE = "authorization";

    // The extension point API version that this plugin understands
    String API_VERSION = "1.0";

    // the identifier of this plugin
    GoPluginIdentifier PLUGIN_IDENTIFIER = new GoPluginIdentifier(EXTENSION_TYPE, Collections.singletonList(API_VERSION));
}
