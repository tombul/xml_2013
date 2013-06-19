<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns="http://www.fu-berlin.de/deeb/WebBrowser" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/root/div[id='question-header']">
    	<name>
	        <xsl:copy-of select="./h1"/>
	    </name>
    </xsl:template>
    
    <xsl:template match="/root/div[id='mainbar']">
    	<xsl:apply-templates select="./div[id='question']/table/tbody/tr"/>
    	<xsl:apply-templates select="./div[id='question']/table/tbody/tr/td/div[class='comments']"/>
    	<xsl:apply-templates select="./div[id='answers']/div[class='answer']"/>
    </xsl:template>
    
    <xsl:template match="td[class='postcell']">
    	<url>
    		<xsl:value-of select="/root/div[id='question-header']/h1/a/@href"/>
    	</url>
    	<description>
    		<xsl:copy-of select="./div/div[itemprop='description']"/>
    	</description>
    	<keywords>
    	    <xsl:copy-of select="./div/div[class='post-taglist']/a"/>
    	</keywords>
    	<datePublished>
   			<xsl:value-of select="./div/table/tbody/tr/td[class='owner']/div/div[class='user-action-time']/span/@title"/>
   		</datePublished>
    	<author>
    		<image>
    			<xsl:copy-of select="./div/table/tbody/tr/td[class='owner']/div/div[class='user-gravatar32']/a/div/img"/>
    		</image>
    		<name>
    			<xsl:value-of select="./div/table/tbody/tr/td[class='owner']/div/div[class='user-details']/a"/>
    		</name>
    		<award>
    			<xsl:copy-of select="./div/table/tbody/tr/td[class='owner']/div/div[class='user-details']/span"/>
    		</award>
    		<url>
    			<xsl:value-of select="./div/table/tbody/tr/td[class='owner']/div/div[class='user-gravatar32']/a/@href"/>
    		</url>
    	</author>
    </xsl:template>
    
    <xsl:template match="div[class='comments']">
        <penis><xsl:copy-of select="."/></penis>
    </xsl:template>
    
    <xsl:template match="div[class='answer']">
        <penis><xsl:copy-of select="."/></penis>
    </xsl:template>
    
    <xsl:template match="text()|@*"/>
</xsl:stylesheet>
