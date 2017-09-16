package dtos;

import java.util.ArrayList;

/**
 * Created by SURAJ on 8/29/2017.
 */

public class GetFillterDTO {

    String branchId, industry;
    ArrayList<JobRollDTO> jobRolllist;

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public ArrayList<JobRollDTO> getJobRolllist() {
        return jobRolllist;
    }

    public void setJobRolllist(ArrayList<JobRollDTO> jobRolllist) {
        this.jobRolllist = jobRolllist;
    }
}
