package utils;

import java.util.ArrayList;

import dtos.CandidateDTO;
import dtos.CompanyDTO;
import dtos.FilterDTO;
import dtos.NotificationDTO;


/**
 * Created by Suraj shakya on 1/3/17.
 */
public class Global {
    public static ArrayList<CompanyDTO> companylist = new ArrayList<>();
    public static ArrayList<CompanyDTO> companySearchlist = new ArrayList<>();
    public static ArrayList<CandidateDTO> candidatelist = new ArrayList<>();
    public static ArrayList<CandidateDTO> searchcandidatelist = new ArrayList<>();
    public static ArrayList<String> skilleslist = new ArrayList<>();
    public static ArrayList<NotificationDTO> notificationList = new ArrayList<>();

    public static ArrayList<FilterDTO> cityList = new ArrayList<>();
    public static ArrayList<FilterDTO> roleList = new ArrayList<>();
    public static ArrayList<FilterDTO> educationList = new ArrayList<>();
    public static ArrayList<FilterDTO> industryList = new ArrayList<>();
}
