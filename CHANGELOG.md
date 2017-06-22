# Changelog

This plugin is bundled along with the GoCD server, hence there will be no separate release of the plugin. Any notable changes would be listed here against the git commit.

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
