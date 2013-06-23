<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns="http://www.fu-berlin.de/deeb/WebBrowser" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
    
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" />
    
    <xsl:template match="/creativeWork">
    	<creativeWork>
	    	<xsl:apply-templates select="./div[@id='question-header']"/>
    		<xsl:apply-templates select="./div[@id='mainbar']"/>
	    </creativeWork>
    </xsl:template>
    
    <xsl:template match="div[@id='question-header']">
    	<name>
	        <xsl:value-of select="./h1/a"/>
	    </name>
   		<xsl:apply-templates select="../div[@id='mainbar']/div[@id='question']/table/tbody/tr/td[@class='postcell']"/>
   		<xsl:apply-templates select="../div[@id='mainbar']/div[@id='question']/table/tbody/tr/td/div[@class='comments']/table/tbody/tr[@class='comment']">
   			<xsl:with-param name="url" select="../div[@id='mainbar']/div[@id='question']/table/tbody/tr/td[@class='postcell']/div/table/tbody/tr/td[@class='vt']/div[@class='post-menu']/a[@class='short-link']/@href"/>
   		</xsl:apply-templates>
    </xsl:template>
    
    <xsl:template match="div[@id='mainbar']">
    	<xsl:apply-templates select="./div[@id='answers']/div[@class='answer']"/>
    </xsl:template>
    
    <xsl:template match="td[@class='postcell']">
    	<url>
    		http://stackoverflow.com<xsl:value-of select="/creativeWork/div[@id='question-header']/h1/a/@href"/>
    	</url>
    	<description>
    		<xsl:copy-of select="./div/div[@itemprop='description']/node()"/>
    	</description>
    	<keywords>
    	    <xsl:copy-of select="./div/div[@class='post-taglist']/a"/>
    	</keywords>
    	<xsl:for-each select="./div/table[@class='fw']/tbody/tr/td">
	    	<xsl:if test="./div/div[@class='user-action-time']/span">
	        	<xsl:call-template name="author"/>
		    </xsl:if>
	    	<xsl:if test="./div/div[@class='user-action-time']/a">
	        	<xsl:call-template name="editor"/>
		    </xsl:if>
	    </xsl:for-each>
    </xsl:template>
    
    <xsl:template name="author" match="td[@class='post-signature']">
    	<datePublished>
   			<xsl:value-of select="./div/div[@class='user-action-time']/span/@title"/>
   		</datePublished>
    	<author>
    		<image>
    			<xsl:copy-of select="./div/div[@class='user-gravatar32']/a/div/img"/>
    		</image>
    		<name>
    			<xsl:value-of select="./div/div[@class='user-details']/a"/>
    		</name>
    		<award>
    			<xsl:copy-of select="./div/div[@class='user-details']/span"/>
    		</award>
    		<url>
    			http://stackoverflow.com<xsl:value-of select="./div/div[@class='user-gravatar32']/a/@href"/>
    		</url>
    	</author>
    </xsl:template>
    
    <xsl:template name="editor" match="td[@class='post-signature']">
    	<dateModified>
   			<xsl:value-of select="./div/div[@class='user-action-time']/a/span/@title"/>
   		</dateModified>
    	<editor>
    		<image>
    			<xsl:copy-of select="./div/div[@class='user-gravatar32']/a/div/img"/>
    		</image>
    		<name>
    			<xsl:value-of select="./div/div[@class='user-details']/a"/>
    		</name>
    		<award>
    			<xsl:copy-of select="./div/div[@class='user-details']/span"/>
    		</award>
    		<url>
    			http://stackoverflow.com<xsl:value-of select="./div/div[@class='user-gravatar32']/a/@href"/>
    		</url>
    	</editor>
    </xsl:template>
    
    <xsl:template match="tr[@class='comment']">
    	<xsl:param name="url"/>
    	<comment>
    		<commentText>
    			<xsl:copy-of select="./td[@class='comment-text']/div/span[@class='comment-copy']/node()"/>
    		</commentText>
    		<commentTime>
    			<xsl:value-of select="./td[@class='comment-text']/div/span[@class='comment-date']/span/@title"/>
    		</commentTime>
    		<discusses>
    			http://stackoverflow.com<xsl:value-of select="$url"/>
    		</discusses>
	    	<creator>
	    		<name>
	    			<xsl:value-of select="./td[@class='comment-text']/div/a[@class='comment-user']"/>
	    		</name>
	    		<award>
	    			<xsl:value-of select="./td[@class='comment-text']/div/a[@class='comment-user']/@title"/>
	    		</award>
	    		<url>
	    			http://stackoverflow.com<xsl:value-of select="./td[@class='comment-text']/div/a[@class='comment-user']/@href"/>
	    		</url>
	    	</creator>
        </comment>
    </xsl:template>
    
    <xsl:template match="div[@class='answer']">
    	<review>
    		<about>
    			http://stackoverflow.com<xsl:value-of select="/creativeWork/div[@id='question-header']/h1/a/@href"/>
    		</about>
    		<url>
    			http://stackoverflow.com<xsl:value-of select="./table/tbody/tr/td[@class='answercell']/table[@class='fw']/tbody/tr/td[@class='vt']/div/a[@class='short-link']/@href"/>
    		</url>
    		<description>
    			<xsl:copy-of select="./table/tbody/tr/td[@class='answercell']/div[@class='post-text']/node()"/>
    		</description>
    		<xsl:for-each select="./table/tbody/tr/td[@class='answercell']/table[@class='fw']/tbody/tr/td[@class='post-signature']">
	    		<xsl:if test="./div/div[@class='user-action-time']/span">
	        		<xsl:call-template name="author"/>
		    	</xsl:if>
	    		<xsl:if test="./div/div[@class='user-action-time']/a">
	        		<xsl:call-template name="editor"/>
		    	</xsl:if>
	    	</xsl:for-each>
    		<xsl:apply-templates select="./table/tbody/tr/td/div[@class='comments']/table/tbody/tr[@class='comment']">
    			<xsl:with-param name="url" select="./table/tbody/tr/td[@class='answercell']/table/tbody/tr/td[@class='vt']/div[@class='post-menu']/a[@class='short-link']/@href"/>
    		</xsl:apply-templates>
        </review>
    </xsl:template>
    
    <xsl:template match="text()|@*"/>
    
</xsl:stylesheet>
