package de.lbe.sandbox.jboss7.ejb31.server;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote(LanguageService.class)
public class LanguageServiceImpl implements LanguageService {

	@Override
	public Language getLanguage(int id) {
		return new Language(id);
	}
}
