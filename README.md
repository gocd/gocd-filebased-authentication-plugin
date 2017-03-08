# GoCD Password File Authentication Plugin
This plugin is a password file based authentication plugin. This allows authentication/search of users defined across multiple password files.  

## Usage instructions
The simplest way to use this plugin is create a plain text file with the following format:

    [username]:[password hashed with SHA1 and encoded with base 64]
    
If your SHA1 algorithm and base 64 encoding works properly, the password "badger" should come out as "ThmbShxAtJepX80c2JY1FzOEmUk=".

You can put as many username/hashed password pairs as you like -- use a new line for each one

The plugin needs to be configured to use the password file, by providing the configuration in cruise_config.xml

* Example Auth Config
```xml
<authConfigs>
  <authConfig id="auth-config-id" pluginId="cd.go.authentication.passwordfile">
    <property>
      <key>PasswordFilePath</key>
      <value>path-to-password-file</value>
    </property>
  </authConfig>
</authConfigs>
```

The plugin can also be configured to use multiple password files if required.
* Example Auth Config
```xml
<authConfigs>
  <authConfig id="auth-config-id-1" pluginId="cd.go.authentication.passwordfile">
    <property>
      <key>PasswordFilePath</key>
      <value>path-to-password-file-1</value>
    </property>
  </authConfig>
  <authConfig id="auth-config-id-2" pluginId="cd.go.authentication.passwordfile">
    <property>
      <key>PasswordFilePath</key>
      <value>path-to-password-file-2</value>
    </property>
  </authConfig>
</authConfigs>
```

## Building the code base

To build the jar, run `./gradlew clean test assemble`

## License

```plain
Copyright 2017 ThoughtWorks, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
