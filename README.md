# GoCD File Based Authentication Plugin

This is a file based authentication plugin which implements the GoCD [Authorization Plugin](https://plugin-api.gocd.io/current/authorization/) endpoint. This plugin allows authentication/search of users defined in a sinlge or across multiple password files.

## Building the code base

To build the jar, run `./gradlew clean test assemble`

## Requirements

These plugins require GoCD version v17.5 or above.

## Installation

- From GoCD `17.5.0` onwards the plugin comes bundled along with server, hence a separate installation is not required.

## Usage instructions

The simplest way to use this plugin is create a plain text file with the following format:

    [username]:[password hashed with SHA1 and encoded with base 64]
    
If your SHA1 algorithm and base 64 encoding works properly, the password "badger" should come out as "ThmbShxAtJepX80c2JY1FzOEmUk=".

> **Note**: This is currently an insecure default, the industry standard recommendation is to use `bcrypt` or `PBKDF2` algorithm. See [#13](https://github.com/gocd/filebased-authentication-plugin/issues/13) for details.   

You can put as many username/hashed password pairs as you like -- use a new line for each one

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

## Generating passwords using htpasswd

You can use the [htpasswd](http://httpd.apache.org/docs/2.0/programs/htpasswd.html) program from Apache to manage your password file.

You must use the -s option with htpasswd to force it to use SHA1 encoding for the password.

So for example, you can use the following command to create a password file called "passwd" and put the password for the user "user" in it:

```shell
htpasswd -c -s passwd user
```

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

## Limitations

This plugin is a as-is replacement for GoCD support for file based authentication. SHA1 is considered insecure and not recommended.
A file based [strong auth plugin](https://github.com/danielsomerfield/go-strong-auth-plugin#go-strong-auth-plugin) was built using the deprecated Authentication endpoints, any contributions towards building a secure file based plugin would be appreciated.

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
