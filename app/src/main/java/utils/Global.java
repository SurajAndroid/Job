package utils;

import java.util.ArrayList;

import dtos.CandidateDTO;
import dtos.CompanyDTO;
import dtos.FilterDTO;
import dtos.GetFillterDTO;
import dtos.MembershipDTO;
import dtos.NotificationDTO;
import dtos.PostDTO;


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

    public static ArrayList<String> jobroll_List = new ArrayList<>();
    public static ArrayList<PostDTO> postJob_List = new ArrayList<>();

    public static ArrayList<MembershipDTO> membershipPack_List = new ArrayList<>();

    public static ArrayList<GetFillterDTO> getFilterList = new ArrayList<>();
}
