# Changelog

## 2.0.0

* [[Authroization Extension v2](https://github.com/gocd/gocd-filebased-authentication-plugin/commit/bc6d5c4d62e80a25af6df80887824d2a5ac2c428)] Implement authorization extension v2 to support:
  - 'get-user-roles' capabilities
  - 'is-valid-user' authorization extension request

## 1.0.3

- [#34](https://github.com/gocd/gocd-filebased-authentication-plugin/pull/34) - Cleaner logs by default + Update dependencies for Java 9 and beyond

  - Logs won't show stack traces by default for a failed authentication. Can turn on DEBUG logging to see stack traces.

## 1.0.2

### Bug fix

- [#20](https://github.com/gocd/gocd-filebased-authentication-plugin/issues/20) - Use utf-8 to read password file

## 1.0.1

### Improved

Cleaned up the build script. No change in functionality.

## 1.0.0

### Improved

Reduced the size of the plugin from 1.2MB to 300KB by removing dependencies on utility code from apache commons. There are no functional changes from the previous release.

## [005bbb2](https://github.com/gocd/gocd-filebased-authentication-plugin/commit/005bbb25e8abd444fdcb3fae1c311ccba53bb3c8) - Bundled with *GoCD v17.6.0*

### Improved

* [#15](https://github.com/gocd/gocd-filebased-authentication-plugin/pull/15) - Added support for passwords hashed using `bcrypt`

### Deprecation

* Passwords hashed using `SHA1` will stop working. Users are encouraged to migrate their password files to use `bcrypt` instead of `SHA1`

## [8493784](https://github.com/gocd/gocd-filebased-authentication-plugin/commit/84937847b9fd113e87d34a1a7f035577c698b580) - Bundled with *GoCD v17.5.0*

### Improved

* Improved overall logging
* Username matching is now case-insensitive

### [v0.1](https://github.com/gocd/gocd-filebased-authentication-plugin/releases/tag/v0.1)


Initial release of plugin.
