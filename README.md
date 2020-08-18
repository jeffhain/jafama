Jafama (Java Fast Math) is a Java library aiming at providing faster versions
of java.lang.Math treatments, at the eventual cost of 1e-15ish accuracy errors
but still handling special cases properly (NaN, +-Infinity, ties, etc.).
It also provides additional features, such as angles normalization methods,
inverse hyperbolic trigonometry, etc.

# License

Apache License V2.0

# Current external dependencies

- src/main:
  - Java 5+

- src/test:
  - Java 6+
  - JUnit 3.8.1

- src/build:
  - Java 6+

# Principal classes

- FastMath, which corresponds to Math API, plus additional treatments.

- StrictFastMath, which is a strict version of FastMath (and not a fast version
  of StrictMath, which would rather be called FastStrictMath), through
  delegation to StrictMath (instead of Math) and use of strictfp.

- NumbersUtils, which contains very low level number-related treatments,
  and is used by (Strict)FastMath.

# Principal additional features, that are not found in Math

- Angles normalization methods (normalizeXxx), derived from those used in
  java.lang.Math, for which, sadly, no API is provided, letting most users
  struggle with their own implementations.

- Direct or inverse hyperbolic trigonometry (coshm1, asinh, acosh, acosh1p,
  atanh, etc.).

- A 3D hypot.

- A remainder(...) method, that unlike Math.remainderIEEE(...), returns a value
  that is the closest to the dividend, and has the same sign, which is more
  consistent across values (doesn't depend on whether values are even or odd),
  with intuition, and with how modulo operator (%) behaves.

- Various floor/ceiling/rounding/modulo related functions.

- (Strict)FastMath.PI_SUP, which is the closest double value superior to
  mathematical Pi, such as 2*PI_SUP angular span fully covers the trigonometric
  circle (2*Math.PI doesn't).

# Miscellaneous

- Methods with same signature than Math ones, are meant to return
  "good" approximations on all range.
  Methods terminating with "Fast" are meant to return "good" approximation
  on a reduced range only.
  Methods terminating with "Quick" are meant to be quick, but do not
  return a good approximation, and might only work on a reduced range.

- Non-redefined methods Math methods (up to some version of the JDK) are also
  available, for easy replacement, even though for some of them, such as for
  incrementExact, you might want to stick to Math versions to benefit from
  eventual JVM intrinsics.

- For performances reasons (for example if wanting to run with no GC),
  these treatments do not (unless delegating to JDK) generate objects at run time.

- Trigonometric treatments make use of look-up tables: around 1 Mo total,
  and initialized lazily and independently, or on first call to
  (Strict)FastMath.initTables().

- Depending on JVM, or JVM options, these treatments can actually be slower
  than Math ones.
  In particular, they can be slower if not optimized by the JIT, which you
  can see with -Xint JVM option.
  Another cause of slowness can be cache-misses on look-up tables.
  Also, look-up tables initialization typically takes multiple hundreds of
  milliseconds (and is about twice slower in J6 than in J5, and in J7 than in
  J6, possibly due to intrinsifications preventing optimizations such as use
  of hardware sqrt, and Math delegating to StrictMath with JIT optimizations
  not yet up during class load).
  As a result, you might want to make these treatments not use tables,
  and delegate to corresponding Math methods, when they are available in the
  lowest supported Java version, by using the appropriate properties (see below).

- About StrictFastMath:
  Unlike StrictMath, StrictFastMath is not supposed to behave identically
  across versions, and even for a same version it might behave differently
  depending on its configuration (properties).
  It is just supposed to behave identically for a same version and
  configuration, which can still be useful, for example for racily computed
  cached values in immutable instances.
  Other than strictness and usually lower performances due to strictfp
  overhead, the only differences with FastMath are its properties, and
  copySign(float,float) and copySign(double,double) semantics.

# Properties for FastMath class treatments

- jafama.usejdk (boolean, default is false):
  If true, for redefined methods, as well as their "Fast" or "Quick"
  terminated counterparts, instead of using redefined computations,
  delegating to Math, when available in required Java version.

- jafama.fastlog (boolean, default is false):
  If true, using redefined computations for log(double) and
  log10(double), else they delegate to Math.log(double) and
  Math.log10(double).
  False by default because Math.log(double) and Math.log10(double)
  seem usually fast (redefined log(double) might be even faster,
  but is less accurate).

- jafama.fastsqrt (boolean, default is false):
  If true, using redefined computation for sqrt(double),
  else it delegates to Math.sqrt(double).
  False by default because Math.sqrt(double) seems usually fast.

# Properties for StrictFastMath class treatments

- jafama.strict.usejdk (boolean, default is false):
  If true, for redefined methods, as well as their "Fast" or "Quick"
  terminated counterparts, instead of using redefined computations,
  delegating to StrictMath, when available in required Java version.

- jafama.strict.fastlog (boolean, default is true):
  If true, using redefined computations for log(double) and
  log10(double), else they delegate to StrictMath.log(double) and
  StrictMath.log10(double).
  True by default because StrictMath.log(double) and
  StrictMath.log10(double) can be quite slow.

- jafama.strict.fastsqrt (boolean, default is false):
  If true, using redefined computation for sqrt(double),
  else it delegates to StrictMath.sqrt(double).
  False by default because StrictMath.sqrt(double) seems usually fast.

# Donation

[![paypal](https://www.paypalobjects.com/en_US/i/btn/btn_donate_SM.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=P7EYEFUCXBS9J)
