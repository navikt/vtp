package no.nav.foreldrepenger.autotest.internal.prototype;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.TestBase;

@Tag("internal")
public class InternalTest extends TestBase{

	@Test
	public void tagTest() {
		System.out.println("Test run");
	}
}
