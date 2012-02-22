package org.mondoaurora.frame.template;

import org.mondoaurora.frame.process.MAFProcess;
import org.mondoaurora.frame.shared.MAFStream;
import org.mondoaurora.frame.shared.MAFVariant;

public interface MAFTemplate extends MAFProcess {
	void writeInto(MAFStream.Out stream, MAFVariant var);
	
	void init(MAFTemplateSyntax syntax);

	// boolean parseFrom(DustStream stream, DustEntity currentEntity) throws Exception;

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