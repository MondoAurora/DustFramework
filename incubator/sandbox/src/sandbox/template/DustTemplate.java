package sandbox.template;

import dust.api.DustConstants;
import dust.api.components.DustEntity;

import sandbox.stream.DustStream;

public interface DustTemplate extends DustConstants {
	void writeInto(DustStream stream, DustEntity currentEntity) throws Exception;

	boolean parseFrom(DustStream stream, DustEntity currentEntity) throws Exception;

	// "EBNF defined in itself" {
	// syntax = [ title ] "{" { production } "}" [ comment ].
	// production = identifier "=" expression ( "." | ";" ) .
	// expression = term { "|" term } .
	// term = factor { factor } .
	// factor = identifier
	// | literal
	// | "[" expression "]"
	// | "(" expression ")"
	// | "{" expression "}" .
	// identifier = character { character } .
	// title = literal .
	// comment = literal .
	// literal = "'" character { character } "'"
	// | '"' character { character } '"' .
	// }
}