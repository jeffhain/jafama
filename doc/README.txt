################################################################################
Jafama 1.2, 2011/03/19, oma

Changes since version 1.1:
- Now using StrictMath to compute constants and look-up tables, to ensure
  consistency across various architectures.
- Now using Math.abs(double) directly instead of FastMath.abs(double), since
  this method is not redefined.
- Added PI_SUP constant, the closest upper approximation of Pi as double,
  especially useful to define a span that covers full angular range
  (2*Math.PI doesn't).
- Added log2(long), log2(int).
- Added odk.fastmath.strict, odk.fastmath.usejdk, odk.fastmath.fastlog and
  odk.fastmath.fastsqrt properties. See FastMath Javadoc for details.
  NB: As a consequence, by default, a redefined log(double) is now used instead
      of Math.log(double), for non-redefined treatments now use StrictMath by
      default, and StrictMath.log(double) seems usually slow.
- Simplified toString() implementation for IntWrapper and DoubleWrapper classes.
- Completed Javadoc and updated tests for FastMath.remainder(double,double)
  method, which does not behave as Math.IEEEremainder(double,double).
- Moved some basic numbers related treatments, into a new class (NumbersUtils),
  since they are very low-level and can be used in many places where a
  dependency to the heavy (look-up tables) FastMath class could be considered
  inappropriate.
  These treatments are still available from FastMath class.
- In benches, made sure dummy variables are used, to avoid treatments to be
  optimized away (has not been observed, but might have been with some JVMs).

################################################################################
Jafama 1.1, 2009/12/05, oma

Changes since version 1.0:
- for asin pow tabs, use of powFast(double,int) instead of pow(double,double),
- added expQuick(double), logQuick(double), powQuick(double),
- changed random numbers computation for tests.

################################################################################
Jafama 1.0, 2009/07/25, oma

- Placed under the GNU Lesser General Public License, version 3.

- Requires Java 1.6 or later.

- src folder contains the code.

- test folder contains some tests (some of which require JUnit).

- The odk.lang package is due to this code being a core part of ODK
  library (Optimized Development Kit, of which only this code is
  open source).

- Copy/paste of FastMath class comments:

 * Class providing math treatments that:
 * - are meant to be faster than those of java.lang.Math class (depending on
 *   JVM or JVM options, they might be slower),
 * - are still somehow accurate and robust (handling of NaN and such),
 * - do not (or not directly) generate objects at run time (no "new").
 * 
 * Other than optimized treatments, a valuable feature of this class is the
 * presence of angles normalization methods, derived from those used in
 * java.lang.Math (for which, sadly, no API is provided, letting everyone
 * with the terrible responsibility to write their own ones).
 * 
 * Non-redefined methods of java.lang.Math class are also available,
 * for easy replacement.
 
################################################################################
