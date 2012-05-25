package br.com.suricattus.surispring.jsf.primefaces.component.input;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@ResourceDependencies({
	@ResourceDependency(library="primefaces", name="primefaces.css"),
	@ResourceDependency(library="primefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="suricattus", name="jquery/jquery.meiomask.js"),
	@ResourceDependency(library="primefaces", name="primefaces.js")
})
public class InputMask extends org.primefaces.component.inputmask.InputMask{

	
	
}
