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

package cd.go.authentication.passwordfile.annotation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FieldMetadata implements Metadata {

    @Expose
    @SerializedName("required")
    private boolean required;

    @Expose
    @SerializedName("secure")
    private boolean secure;

    private FieldType type;

    public FieldMetadata(boolean required, boolean secure, FieldType type) {
        this.required = required;
        this.secure = secure;
        this.type = type;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public FieldType getType() {
        return type;
    }
}
