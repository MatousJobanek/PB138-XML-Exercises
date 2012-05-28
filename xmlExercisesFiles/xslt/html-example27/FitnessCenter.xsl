<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
 
    <xsl:output method="html"/>

    <xsl:template match="/">
        <xsl:message>
            XSLT Processor: <xsl:value-of select="system-property('xsl:vendor')"/>
        </xsl:message>
        <HTML>
            <HEAD>
                <TITLE>Welcome</TITLE>
            </HEAD>
            <BODY>
                Welcome!
            </BODY>
        </HTML>
    </xsl:template>

</xsl:stylesheet>