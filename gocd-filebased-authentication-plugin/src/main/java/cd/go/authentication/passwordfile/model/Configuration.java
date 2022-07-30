/*
 * Copyright 2022 Thoughtworks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cd.go.authentication.passwordfile.model;

import cd.go.authentication.passwordfile.annotation.MetadataHelper;
import cd.go.authentication.passwordfile.annotation.ProfileField;
import cd.go.authentication.passwordfile.utils.Util;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Configuration {

    @Expose
    @SerializedName("PasswordFilePath")
    @ProfileField(key = "PasswordFilePath", required = true, secure = false)
    private String passwordFilePath;

    public static Configuration fromJSON(String json) {
        return Util.GSON.fromJson(json, Configuration.class);
    }

    public String getPasswordFilePath() {
        return passwordFilePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Configuration that = (Configuration) o;

        return passwordFilePath != null ? passwordFilePath.equals(that.passwordFilePath) : that.passwordFilePath == null;
    }

    @Override
    public int hashCode() {
        return passwordFilePath != null ? passwordFilePath.hashCode() : 0;
    }

    public static List<Map<String, String>> validate(Map<String, String> configuration) {
        final List<Map<String, String>> validationResult = MetadataHelper.validate(Configuration.class, configuration);
        return validationResult;
    }
}
