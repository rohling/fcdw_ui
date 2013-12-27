// FUNcube Data Warehouse
// Copyright 2013 (c) David A.Johnson, G4DPZ, AMSAT-UK
// This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter
// to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.

package uk.org.funcube.fcdw.service.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.org.funcube.fcdw.server.dao.HighPrecisionDao;
import uk.org.funcube.fcdw.server.model.HPEntity;
import uk.org.funcube.fcdw.server.service.impl.AbstractService;
import uk.org.funcube.fcdw.server.shared.DataElement;
import uk.org.funcube.fcdw.server.shared.HighResJson;


@Controller
@RequestMapping(value = "/ui/highres")
public class HighResServiceRestImpl extends AbstractService {
	
	@Autowired
	HighPrecisionDao highResolutionDao;
	
	// get all data for one orbit for a given satellite
	@Transactional(readOnly = true)
	@RequestMapping(value = "/{satelliteId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public HighResJson getAllHighResForSatellite(
			@PathVariable(value = "satelliteId") Long satelliteId) {
		

		HighResJson highResJson = new HighResJson();
		
		DataElement channel1 = new DataElement("Sun Sensor +X");
		highResJson.addElement(channel1);
		DataElement channel2 = new DataElement("Sun Sensor +Y");
		highResJson.addElement(channel2);
		DataElement channel3 = new DataElement("Sun Sensor -Y");
		highResJson.addElement(channel3);
		DataElement channel4 = new DataElement("Sun Sensor +Z");
		highResJson.addElement(channel4);
		DataElement channel5 = new DataElement("Sun Sensor -Z");
		highResJson.addElement(channel5);
		DataElement channel6 = new DataElement("Total Photo Current");
		highResJson.addElement(channel6);
		DataElement channel7 = new DataElement("Battery Volts");
		highResJson.addElement(channel7);
		
		List<HPEntity> latestOrbit = highResolutionDao.getLatestFourMinutes(satelliteId);
		
		for (HPEntity highResEntity : latestOrbit) {
			for (int i = 0; i < 10; i++) {
				switch (i) {
				case 0: 
					channel1.addDatum(new Double(SOL_ILLUMINATION[highResEntity.getC1().intValue()]));
					break;
				case 1: 
					channel2.addDatum(new Double(SOL_ILLUMINATION[highResEntity.getC2().intValue()]));
					break;
				case 2: 
					channel3.addDatum(new Double(SOL_ILLUMINATION[highResEntity.getC3().intValue()]));
					break;
				case 3: 
					channel4.addDatum(new Double(SOL_ILLUMINATION[highResEntity.getC4().intValue()]));
					break;
				case 4: 
					channel5.addDatum(new Double(SOL_ILLUMINATION[highResEntity.getC5().intValue()]));
					break;
				case 5: 
					channel6.addDatum(new Double(highResEntity.getC6()*2.0));
					break;
				case 6: 
					channel7.addDatum(new Double(highResEntity.getC7()*2.0));
					break;
				}
			}
		}
		
		
		return highResJson;
		
	}
	
}
