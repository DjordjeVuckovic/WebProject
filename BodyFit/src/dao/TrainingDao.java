package dao;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import beans.Manager;
import beans.Trainer;
import beans.Training;

public class TrainingDao extends GenericDao<String, Training, TrainingDao> {
	TrainerDao trainerDao = new TrainerDao();
	@Override
	protected String getKey(Training entity) {
		// TODO Auto-generated method stub
		return entity.getId();
	}

	@Override
	protected String getFilePath() {
		// TODO Auto-generated method stub
		return "trainings.json";
	}
	public Map<String,Training> getAllToMap(){
		String json = "";
		try {
			json = new String(Files.readAllBytes(Paths.get(getPath())));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Type empMapType = new TypeToken<Map<String, Training>>() {
		}.getType();

		Map<String, Training> map = gs.fromJson(json, empMapType);
		return map;
		
	}

	@Override
	public ArrayList<Training> getAllToList() {
		// TODO Auto-generated method stub
		return new ArrayList<Training>(getAllToMap().values());
	}
	public ArrayList<Training> getAllByManager(String managerFacilityId){
		ArrayList<Training> trainingsForManager = new ArrayList<Training>();
		for (Training training : getAllToList()) {
			if(training.getSportFacilityId().equals(managerFacilityId)) {
				trainingsForManager.add(training);
			}
			
		}
		return trainingsForManager;
	}
//	public ArrayList<Trainer> getAllTrainersForTrainings(String  managerFacilityId){
//		ArrayList<Trainer> trainers = new ArrayList<Trainer>();
//		for(Training training: getAllByManager(managerFacilityId)) {
//			if(!training.getTrainerId().isEmpty()) {
//				Trainer trainer =  trainerDao.getById(training.getTrainerId());
//				trainers.add(trainer);
//			}
//			
//		}
//		return trainers; 
//	}

}
