<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:oai="http://www.openarchives.org/OAI/2.0/" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:deeb="http://www.fu-berlin.de/deebWebBrowser/">
	
	<xsl:output method="xml" indent="yes" />
	
	<xsl:template match="oai:OAI-PMH">
		<deeb:publications>
			<xsl:for-each select="./oai:ListRecords/oai:record/oai:metadata/oai_dc:dc">
				<deeb:publication xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:deeb="http://www.fu-berlin.de/deebWebBrowser/">
					<xsl:apply-templates select="." />
				</deeb:publication>
		    </xsl:for-each>
	    </deeb:publications>
	</xsl:template>
	
    <xsl:template match="dc:identifier">
    	<dc:identifier>
	    	<xsl:value-of select="../../../oai:header/oai:identifier" />
	   	</dc:identifier>	
    </xsl:template>
	
    <xsl:template match="dc:title">
    	<xsl:copy-of select="." />	
    </xsl:template>
    
    <xsl:template match="dc:creator">
    	<xsl:copy-of select="."/>	
    </xsl:template>
    
    <xsl:template match="dc:subject">
    	<xsl:copy-of select="."/>	
    </xsl:template>
    
    <xsl:template match="dc:description">
    	<xsl:copy-of select="."/>	
    </xsl:template>
    
    <xsl:template match="dc:publisher">
    	<xsl:copy-of select="."/>	
    </xsl:template>
    
    <xsl:template match="dc:date">
    	<xsl:copy-of select="."/>
    </xsl:template>
    
	<xsl:template match="text()|@*" />
</xsl:stylesheet>
