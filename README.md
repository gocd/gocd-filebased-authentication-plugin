# GoCD File Based Authentication Plugin

This is a file based authentication plugin which implements the GoCD [Authorization Plugin](https://plugin-api.gocd.io/current/authorization/) endpoint. This plugin allows authentication/search of users defined in password files.

## Building the code base

To build the jar, run `./gradlew clean test assemble`

## Installation

- From GoCD `17.5.0` onwards the plugin comes bundled along with server, hence a separate installation is not required.

## Usage instructions

The plugin uses the popular file format used by the `htpasswd` program:

    username:hashed-password

The plugin currently supports passwords hashed using `bcrypt`, `PBKDF2` and `SHA1`. See [#13](https://github.com/gocd/gocd-filebased-authentication-plugin/issues/13) for details.

> **Note**: It is highly recommended that users use passwords hashed using `bcrypt` and not `SHA1`.

You can put as many username/hashed password pairs as you like -- use a new line for each one

## Generating password using cli-app

You can use the [gocd-passwd](https://github.com/gocd/gocd-filebased-authentication-plugin/tree/master/gocdpasswd) to generate hashed password file entry.

Use the following command to generate bcrypt hashed password file entry,  

```bash
$ java -jar gocdpasswd.jar -B -u username -p password
```

Where, `-B` forces password hashing with bcrypt, However you can also use `-P` for PBKDF2SHA1 and `-S` for PBKDF2SHA256

## Generating passwords using `htpasswd`

You can use the [htpasswd](http://httpd.apache.org/docs/2.0/programs/htpasswd.html) program from Apache to manage your password file.

So for example, you can use the following command to create a password file called `passwd` and put the password for the user "user" in it:

```shell
htpasswd -c -B passwd user
```
Where, `-B` forces password hashing with bcrypt, currently considered to be very secure. You can also use `-s` for SHA or `-d` for crypt() but these are deemed insecure

### htpasswd on Windows

The `htpasswd` executable may be downloaded as part of the apache distribution from [Apache Haus](http://www.apachehaus.com/cgi-bin/download.plx) or [Apache Lounge](https://www.apachelounge.com/download/). Note that these executables may not work on Windows XP or Server 2003

### htpasswd on Mac OSX

htpasswd is already installed by default on Mac OSX.

### htpasswd on Linux

Debian based distributions (e.g. Ubuntu) htpasswd can be installed from the apache2-utils

```shell
$ apt-get install apache2-utils
```

### Generating passwords using python

Another option is to use the following command (assumes python is installed on your system)

```shell
$ python -c "import sha; from base64 import b64encode; print b64encode(sha.new('my-password').digest())"
```

## Configuration

The plugin needs to be configured to use the password file. The configuration can be added by adding a Authorization Configuration by visiting the Authorization Configuration page under *Admin > Security*.

Alternatively, the configuration can be added directly to the `config.xml` using the `<authConfig>` configuration.

* Example Configuration

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

* The plugin can also be configured to use multiple password files if required:

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

## Troubleshooting

### Verify Connection

For a given Authorization Configuration verify if the given password file can be accessed by the plugin. The Authorization Configuration page under *Admin > Security* gives an option to verify connection.

### Enable Debug Logs

* On Linux:

    Enabling debug level logging can help you troubleshoot an issue with this plugin. To enable debug level logs, edit the file `/etc/default/go-server` (for Linux) to add:

    ```shell
    export GO_SERVER_SYSTEM_PROPERTIES="$GO_SERVER_SYSTEM_PROPERTIES -Dplugin.cd.go.authentication.passwordfile.log.level=debug"
    ```

    If you're running the server via `./server.sh` script:

    ```shell
    $ GO_SERVER_SYSTEM_PROPERTIES="-Dplugin.cd.go.authentication.passwordfile.log.level=debug" ./server.sh
    ```

* On windows:

    Edit the file `config/wrapper-properties.conf` inside the GoCD Server installation directory (typically `C:\Program Files\Go Server`):

    ```
    # config/wrapper-properties.conf
    # since the last "wrapper.java.additional" index is 15, we use the next available index.
    wrapper.java.additional.16=-Dplugin.cd.go.authentication.passwordfile.log.level=debug
    ```

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
