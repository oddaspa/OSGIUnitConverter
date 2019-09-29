package no.tdt4250.assignment2.conversionApp.no;

import org.osgi.service.component.annotations.Component;

import no.tdt4250.assignment2.conversionApp.api.Dict;
import no.tdt4250.assignment2.conversionApp.util.WordsDict;

@Component(
		property = {
				WordsDict.DICT_NAME_PROP + "=kp",
				WordsDict.DICT_RESOURCE_PROP + "=no.tdt4250.assignment2.conversionApp.no#/no/tdt4250/assignment2/conversionApp/no/kilotopound.txt"}
		)
public class NbDict extends WordsDict implements Dict {
}
