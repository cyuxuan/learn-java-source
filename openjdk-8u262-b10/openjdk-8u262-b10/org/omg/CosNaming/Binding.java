package org.omg.CosNaming;


/**
* org/omg/CosNaming/Binding.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from c:/openjdk/jdk8u/jdk8u/corba/src/share/classes/org/omg/CosNaming/nameservice.idl
* Saturday, July 25, 2020 1:24:06 AM EDT
*/

public final class Binding implements org.omg.CORBA.portable.IDLEntity
{
  public org.omg.CosNaming.NameComponent binding_name[] = null;

  // name
  public org.omg.CosNaming.BindingType binding_type = null;

  public Binding ()
  {
  } // ctor

  public Binding (org.omg.CosNaming.NameComponent[] _binding_name, org.omg.CosNaming.BindingType _binding_type)
  {
    binding_name = _binding_name;
    binding_type = _binding_type;
  } // ctor

} // class Binding
