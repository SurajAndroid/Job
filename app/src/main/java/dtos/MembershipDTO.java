package dtos;

/**
 * Created by Admin1 on 9/13/2017.
 */

public class MembershipDTO {
    String id,package_name,candidate_count,post_job_count,discription,package_price,created_at,validFor, download, post_job, apply, package_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getPost_job() {
        return post_job;
    }

    public void setPost_job(String post_job) {
        this.post_job = post_job;
    }

    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }

    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getCandidate_count() {
        return candidate_count;
    }

    public void setCandidate_count(String candidate_count) {
        this.candidate_count = candidate_count;
    }

    public String getPost_job_count() {
        return post_job_count;
    }

    public void setPost_job_count(String post_job_count) {
        this.post_job_count = post_job_count;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getPackage_price() {
        return package_price;
    }

    public void setPackage_price(String package_price) {
        this.package_price = package_price;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getValidFor() {
        return validFor;
    }

    public void setValidFor(String validFor) {
        this.validFor = validFor;
    }
}
