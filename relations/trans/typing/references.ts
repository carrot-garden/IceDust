module typing/references

imports

	// constructors
	src-gen/signatures/Data-sig
	src-gen/signatures/Expressions-sig
	src-gen/signatures/Model-sig
	src-gen/signatures/Types-sig
	trans/api/constructors
	trans/desugaring/constructors
	
	// functions
	typing/_multiplicity-functions
	trans/naming/names
	
	// // use custom runtime libraries  
 //  lib/nabl/-
 //  lib/task/-
 //  lib/types/-
 //  lib/properties/-
 //  lib/relations/-

type rules // general references

	MemberAccess(expr, member) : member-ty
	where definition of member : member-ty
	
	MemberAccess(expr, member) has multiplicity mu
	where	expr has multiplicity expr-mu
		and definition of member has multiplicity member-mu
		and <cartesian-product> (expr-mu, member-mu) => mu

	Ref(a) : ty
	where definition of a : ty
	
	Ref(a) has multiplicity a-mu
	where definition of a has multiplicity a-mu

	this@This() : ty
	where definition of this : ty 

	This() has multiplicity One()

type rules // specific references

	RoleRef(r) : r-ty
	where definition of r : r-ty
	
	AttributeRef(a) : a-ty
	where definition of a : a-ty
	
	EntityInstanceRef(e) : e-ty
	where definition of e : e-ty

