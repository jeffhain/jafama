
2009/07/25, oma

- JavaFastMath 1.0, placed under the GNU Lesser General Public License, version 3.

- Requires Java 1.6 or later.

- src folder contains the code.

- test folder contains some tests (some of which require JUnit).

- The odk.lang package is due to this code being a core part of ODK
  library (Optimized Development Kit, of which only this code is
  open source).

- Copy/paste of FastMath class comments:
################################################################################
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
