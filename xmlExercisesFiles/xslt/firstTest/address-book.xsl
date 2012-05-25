<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>

    <xsl:output 
        method="html" encoding="UTF-8" indent="yes"
        doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
        doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" />


    <xsl:template match="/">
        <html>
            <head>
                <title>Addressbook</title>
            </head>
            <body>
                <h1>Addressbook</h1>
                <b>Quick links to people</b>
                <ul>
                    <xsl:for-each select="//person">
                        <xsl:if test="@id">
                            <li>
                                <a href="#{@id}">
                                    <xsl:call-template name="createContactInfo">
                                        <xsl:with-param name="idOfContact" select="@id"/>
                                    </xsl:call-template>
                                </a>
                            </li>
                        </xsl:if>
                    </xsl:for-each>
                </ul>
                <xsl:for-each select="//person">
                    <xsl:if test="@id">
                        <hr/>
                        <h2>
                            <xsl:value-of select="concat(./firstname, ' ', ./surname)"/>
                        </h2>
                        <a name="{@id}"/>
                        <xsl:for-each select="./e-mail">
                            <xsl:if test="text()">
                                e-mail: 
                                <a href="mailto:{text()}">
                                    <xsl:value-of select="text()"/>
                                </a>
                                <br/>
                            </xsl:if>
                        </xsl:for-each>            
                    
                        <xsl:choose>
                            <xsl:when test="not(./contacts) or count(./contacts/contact) = 0">
                                <i>No contacts</i>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:choose>
                                    <xsl:when test="./contacts[@visible='true']">
                                        <xsl:for-each select="./contacts/contact">
                                            <xsl:choose>
                                                <xsl:when test="@visible='false'">
                                                    <i>An invisible contact</i>
                                                    <br/>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    Contact: 
                                                    <xsl:call-template name="createContactInfo">
                                                        <xsl:with-param name="idOfContact" select="@person-ref"/>
                                                    </xsl:call-template>
                                                    <br/>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:for-each>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <i>Some contacts but not visible</i>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:if>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>
    
                                        

    <xsl:template name="createContactInfo">
        <xsl:param name="idOfContact"/>
        <b>
            <xsl:value-of select="concat(//person[@id=$idOfContact]/firstname, ' ', //person[@id=$idOfContact]/surname)"/>
        </b>
        <xsl:value-of select="concat(' (', $idOfContact, ')')"/>
    </xsl:template>

</xsl:stylesheet>
