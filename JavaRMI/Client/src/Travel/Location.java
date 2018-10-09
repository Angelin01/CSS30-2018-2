package Travel;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Location {
	ARACAJU("Aracaju"),
	BELEM("Belem"),
	BELO_HORIZONTE("Belo Horizonte"),
	BOA_VISTA("Boa Vista"),
	BRASILIA("Brasilia"),
	CAMPO_GRANDE("Campo Grande"),
	CUIABA("Cuiaba"),
	CURITIBA("Curitiba"),
	FLORIANOPOLIS("Florianopolis"),
	FORTALEZA("Fortaleza"),
	GOAIANA("Goiana"),
	MACAPA("Macapa"),
	MACEIO("Maceio"),
	MANAUS("Manaus"),
	NATAL("Natal"),
	PALMAS("Palmas"),
	PORTO_ALEGRE("Porto Alegre"),
	PORTO_VELHO("Porto Velho"),
	RECIFE("Recife"),
	RIO_BRANCO("Rio Branco"),
	RIO_DE_JANEIRO("Rio de Janeiro"),
	SALVADOR("Salvador"),
	SAO_LUIS("Sao Luis"),
	SAO_PAULO("Sao Paulo"),
	TERESINA("Teresina"),
	VITORIA("Vitoria");

	private final String name;

	Location(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Returns a String of all locations separated by a comma
	 * @return the String of all locations
	 */
	public static String printAll() {
		return Stream.of(Location.values()).
				map(Location::name).
				collect(Collectors.joining(", "));
	}
}