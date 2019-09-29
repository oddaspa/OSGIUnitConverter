package no.tdt4250.assignment2.conversionApp.gogo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import no.tdt4250.assignment2.conversionApp.api.Dict;
import no.tdt4250.assignment2.conversionApp.api.DictSearchResult;
import no.tdt4250.assignment2.conversionApp.util.WordsDict;

// see https://enroute.osgi.org/FAQ/500-gogo.html

@Component(
		service = DictCommands.class,
		property = {
			"osgi.command.scope=dict",
			"osgi.command.function=list",
			"osgi.command.function=lookup",
			"osgi.command.function=add",
			"osgi.command.function=remove"
		}
	)
public class DictCommands {

	private Configuration getConfig(String dictName) {
		try {
			Configuration[] configs = cm.listConfigurations("(&(" + WordsDict.DICT_NAME_PROP + "=" + dictName + ")(service.factoryPid=" + WordsDict.FACTORY_PID + "))");
			if (configs != null && configs.length >= 1) {
				return configs[0];
			}
		} catch (IOException | InvalidSyntaxException e) {
		}
		return null;
	}

	@Descriptor("list available dictionaries")
	public void list() {
		System.out.print("Dictionaries: ");
		BundleContext bc = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		try {
			for (ServiceReference<Dict> serviceReference : bc.getServiceReferences(Dict.class, null)) {
				Dict dict = bc.getService(serviceReference);
				try {
					if (dict != null) {
						System.out.print(dict.getDictName());
						if (getConfig(dict.getDictName()) != null) {
							System.out.print("*");						
						}
					}
				} finally {
					bc.ungetService(serviceReference);
				}
				System.out.print(" ");
			}
		} catch (InvalidSyntaxException e) {
		}
		System.out.println();
	}

	@Descriptor("look up a word in each available dictionary")
	public void lookup(
			@Descriptor("the word to look up")
			String s
			) {
		BundleContext bc = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		try {
			// iterate through all Dict service objects
			for (ServiceReference<Dict> serviceReference : bc.getServiceReferences(Dict.class, null)) {
				Dict dict = bc.getService(serviceReference);
				if (dict != null) {
					try {
						DictSearchResult search = dict.search(s);
						System.out.println(dict.getDictName() + ": " + search.getMessage());
					} finally {
						bc.ungetService(serviceReference);
					}
				} else {
					System.out.println(serviceReference.getProperties());
				}
			}
		} catch (InvalidSyntaxException e) {
		}
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	private ConfigurationAdmin cm;

	@Descriptor("add a dictionary, with content from a URL and/or specific words")
	public void add(
			@Descriptor("the name of the new dictionary")
			String name,
			@Descriptor("the URL of file with the words, or a simple word to add to the dictionary")
			String urlStringOrWord,
			@Descriptor("additional words to add to the dictionary")
			String... ss
			) throws IOException, InvalidSyntaxException {
		URL url = null;
		Collection<String> words = new ArrayList<String>();
		try {
			url = new URL(urlStringOrWord);
		} catch (MalformedURLException e) {
			words.add(urlStringOrWord);
		}
		words.addAll(Arrays.asList(ss));
		String actionName = "updated";
		// lookup existing configuration
		Configuration config = getConfig(name);
		if (config == null) {
			// create a new one
			config = cm.createFactoryConfiguration(WordsDict.FACTORY_PID, "?");
			actionName = "added";
		}
		Dictionary<String, String> props = new Hashtable<>();
		props.put(WordsDict.DICT_NAME_PROP, name);
		if (url != null) {
			props.put(WordsDict.DICT_RESOURCE_PROP, url.toString());
		}
		if (words != null && words.size() > 0) {
			props.put(WordsDict.DICT_WORDS_PROP, String.join(" ", words));
		}
		config.update(props);
		System.out.println("\"" + name + "\" dictionary " + actionName);
	}

	@Descriptor("remove a (manually added) dictionary")
	public void remove(
			@Descriptor("the name of the (manually added) dictionary to remove")
			String name
			) throws IOException, InvalidSyntaxException {
		Configuration config = getConfig(name);
		boolean removed = false;
		if (config != null) {
			config.delete();
			removed = true;
		}
		System.out.println("\"" + name + "\" dictionary " + (removed ? "removed" : "was not added manually"));
	}
}
