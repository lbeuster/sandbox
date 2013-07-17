package de.lbe.cal10n;

import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.hasSize;

import org.junit.Test;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import ch.qos.cal10n.MessageConveyorException;
import ch.qos.cal10n.verifier.Cal10nError;
import ch.qos.cal10n.verifier.IMessageKeyVerifier;
import ch.qos.cal10n.verifier.MessageKeyVerifier;

import com.zanox.lib.commons.test.junit.AbstractJUnit4Test;

/**
 * Orika say that we should use the
 * 
 * @author lars.beuster
 */
public class ColorsTest extends AbstractJUnit4Test {

	/**
	 * 
	 */
	@Test
	public void testEnLocale() {
		IMessageConveyor messages = new MessageConveyor(Locale.UK);
		String red = messages.getMessage(Colors.RED);
		assertEquals("red_en", red);
	}

	/**
	 * 
	 */
	@Test(expected = MessageConveyorException.class)
	public void testNoDefaultLocale() {
		// we don't have a fr bundle, but a default bundle
		IMessageConveyor messages = new MessageConveyor(Locale.FRANCE);
		messages.getMessage(Colors.RED);
	}

	/**
	 * 
	 */
	@Test
	public void testVerifyAllLocales() {
		IMessageKeyVerifier mkv = new MessageKeyVerifier(Colors.class);
		List<Cal10nError> errors = mkv.verify(Locale.ENGLISH);
		assertThat("errors: " + errors, errors, hasSize(0));
	}

	/**
	 * 
	 */
	@Test
	public void testEnumWithoutLocaleData() {
		IMessageKeyVerifier mkv = new MessageKeyVerifier(EnumWithoutLocaleData.class);
		List<Cal10nError> errors = mkv.verifyAllLocales();
		assertThat("errors: " + errors, errors, hasSize(1));
		Cal10nError error = errors.get(0);
		assertEquals("MISSING_LOCALE_DATA_ANNOTATION", ((Enum<?>) error.getErrorType()).name());
	}

	/**
	 * 
	 */
	@BaseName("enumWithoutLocaleData")
	public enum EnumWithoutLocaleData {
		ELEM1
	}
}
