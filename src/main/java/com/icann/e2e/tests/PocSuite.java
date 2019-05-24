package com.icann.e2e.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.icann.cms.tests.CmsPoc;
import com.icann.dms.tests.DmsPoc;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	//list test cases sequentially - they will be run in order.  if you want suite specific data, it can be specified in the static Suite class.
	DmsPoc.class, 
	CmsPoc.class
})

public class PocSuite{
	
}

