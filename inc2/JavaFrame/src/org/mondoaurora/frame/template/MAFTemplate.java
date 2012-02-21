package org.mondoaurora.frame.template;

import org.mondoaurora.frame.kernel.MAFKernelEntity;
import org.mondoaurora.frame.shared.MAFStream;

public interface MAFTemplate {
	void writeInto(MAFStream.Out stream, MAFKernelEntity currentEntity) throws Exception;

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