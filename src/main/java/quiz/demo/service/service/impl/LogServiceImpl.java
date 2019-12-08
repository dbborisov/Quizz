package quiz.demo.service.service.impl;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quiz.demo.data.model.Log;
import quiz.demo.data.repository.LogRepository;
import quiz.demo.service.model.LogServiceModel;
import quiz.demo.service.service.LogService;

@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LogServiceImpl(LogRepository logRepository, ModelMapper modelMapper) {
        this.logRepository = logRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public LogServiceModel seedLogInDB(LogServiceModel logServiceModel) {
        Log log = this.modelMapper.map(logServiceModel, Log.class);
        return this.modelMapper.map(this.logRepository.saveAndFlush(log),LogServiceModel.class);
    }
}
