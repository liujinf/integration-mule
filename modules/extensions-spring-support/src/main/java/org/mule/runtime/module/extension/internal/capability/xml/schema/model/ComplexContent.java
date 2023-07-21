/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.2-hudson-jaxb-ri-2.2-63- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.06.14 at 03:58:12 PM GMT-03:00 
//


package org.mule.runtime.module.extension.internal.capability.xml.schema.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for anonymous complex type.
 * <p/>
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated">
 *       &lt;choice>
 *         &lt;element name="restriction" type="{http://www.w3.org/2001/XMLSchema}complexRestrictionType"/>
 *         &lt;element name="extension" type="{http://www.w3.org/2001/XMLSchema}extensionType"/>
 *       &lt;/choice>
 *       &lt;attribute name="mixed" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"restriction", "extension"})
@XmlRootElement(name = "complexContent")
public class ComplexContent extends Annotated {

  protected ComplexRestrictionType restriction;
  protected ExtensionType extension;
  @XmlAttribute(name = "mixed")
  protected Boolean mixed;

  /**
   * Gets the value of the restriction property.
   *
   * @return possible object is {@link ComplexRestrictionType }
   */
  public ComplexRestrictionType getRestriction() {
    return restriction;
  }

  /**
   * Sets the value of the restriction property.
   *
   * @param value allowed object is {@link ComplexRestrictionType }
   */
  public void setRestriction(ComplexRestrictionType value) {
    this.restriction = value;
  }

  /**
   * Gets the value of the extension property.
   *
   * @return possible object is {@link ExtensionType }
   */
  public ExtensionType getExtension() {
    return extension;
  }

  /**
   * Sets the value of the extension property.
   *
   * @param value allowed object is {@link ExtensionType }
   */
  public void setExtension(ExtensionType value) {
    this.extension = value;
  }

  /**
   * Gets the value of the mixed property.
   *
   * @return possible object is {@link Boolean }
   */
  public Boolean isMixed() {
    return mixed;
  }

  /**
   * Sets the value of the mixed property.
   *
   * @param value allowed object is {@link Boolean }
   */
  public void setMixed(Boolean value) {
    this.mixed = value;
  }

}
