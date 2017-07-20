# Changelog

## 1.0.0

### Improved

Reduced the size of the plugin from 1.2MB to 300KB by removing dependencies on utility code from apache commons. There are no functional changes from the previous release.

## [005bbb2](https://github.com/gocd/filebased-authentication-plugin/commit/005bbb25e8abd444fdcb3fae1c311ccba53bb3c8) - Bundled with *GoCD v17.6.0*

### Improved

* [#15](https://github.com/gocd/filebased-authentication-plugin/pull/15) - Added support for passwords hashed using `bcrypt`

### Deprecation

* Passwords hashed using `SHA1` will stop working. Users are encouraged to migrate their password files to use `bcrypt` instead of `SHA1`

## [8493784](https://github.com/gocd/filebased-authentication-plugin/commit/84937847b9fd113e87d34a1a7f035577c698b580) - Bundled with *GoCD v17.5.0*

### Improved

* Improved overall logging
* Username matching is now case-insensitive

### [v0.1](https://github.com/gocd/filebased-authentication-plugin/releases/tag/v0.1)


Initial release of plugin.
