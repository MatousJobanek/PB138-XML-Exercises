<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
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
