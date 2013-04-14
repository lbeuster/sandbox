package de.lbe.sandbox.openejb;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote(LanguageService.class)
public class LanguageServiceImpl implements LanguageService {

	@EJB
	LanguageDao languageBean;

	@Override
	public Language getLanguage(int id) {
		return languageBean.findByPrimaryKey(id);
	}
}
