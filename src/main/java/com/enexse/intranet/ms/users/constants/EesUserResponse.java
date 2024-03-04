package com.enexse.intranet.ms.users.constants;

public class EesUserResponse {

    // SUBJECT MAIL
    public static final String EES_SUBJECT_EMAIL_FORGOT_PASSWORD = "Confirmation email reset your password";
    public static final String EES_SUBJECT_EMAIL_VERIFICATION = "Confirmation send verification email";
    public static final String EES_SUBJECT_INVITATION_USER = "Invitation to connect on Intranet Enexse";
    public static final String EES_SUBJECT_CERTIFICATE_EMAIL = "New user in EES Intranet - Identify your email %s";
    public static final String EES_SUBJECT_FORGOT_PASSWORD_USER = "Invitation to change your password";
    public static final String EES_SUBJECT_UPDATE_REQUEST_STATUS = "Update on your user creation request";
    public static final String EES_SUBJECT_CODE_AUTHENTICATION = "Verification email code";

    // ACTIONS TYPE MESSAGE MAIL
    public static final String EES_MAIL_INVITATION_USER = "Your credential to Enexse Intranet ";
    public static final String EES_MAIL_FORGOT_PASSWORD_USER = "Change your password ";

    // ROLES
    public static final String EES_ROLE_CREATED = "Role created successfully.";
    public static final String EES_ROLE_ALREADY_EXISTS = "Role already exists with code %s. Try another one.";
    public static final String EES_ROLE_DELETED = "Role deleted successfully .";
    public static final String EES_ROLE_NOT_FOUND = "Role not found with ";
    public static final String EES_UPDATE_BY_CODE_ROLE = "Role updated successfully.";
    public static final String EES_ROLE_INVALID_LENGTH = "RoleCode must be EES-XXX.";
    public static final String EES_ROLE_PREFIX = "EES-ROLE-";

    // GROUPS
    public static final String EES_GROUP_PREFIX = "EES-GROUP-";
    public static final String EES_GROUP_INVALID_LENGTH = "RoleCode must be EES-GROUP-XXX.";
    public static final String EES_GROUP_ALREADY_EXISTS = "Group already exists with code %s. Try another one.";
    public static final String EES_GROUP_CREATED = "Group created successfully.";
    public static final String EES_GROUP_NOT_FOUND = "Group not found with ";
    public static final String EES_GROUP_DELETED = "Group deleted successfully .";
    public static final String EES_UPDATE_BY_CODE_GROUP = "Group updated successfully.";
    public static final String EES_SEND_MAIL_GROUP = "Email sent successfully to group.";

    // USERS
    public static final String EES_USER_CREATED = "User created successfully.";
    public static final String EES_USER_ALREADY_EXISTS = "User already exists with the Personal email %s in our intranet Enexse. Try to contact the administration system.";
    public static final String EES_MATRICULE_ALREADY_EXISTS = "matricule %s already exists in database!!";
    public static final String EES_USER_NOT_FOUND = "User not found with %s";
    public static final String EES_USER_CERTIFICATION_LINK = "Confirmation link to certify your identity successfully send to %s";
    public static final String EES_USER_FORGOT_PASSWORD_LINK = "Confirmation link to change your password successfully send to %s";
    public static final String EES_USER_TWO_FACTORY_CODE = "Confirmation code to process successfully send to %s";
    public static final String EES_USER_DOES_NOT_EXISTS = "User does not exists";
    public static final String EES_USER_LINK_ALREADY_SENT = "we have already send you a link";
    public static final String EES_USER_ARCHIVED = "User archived successfully ";
    public static final String EES_USER_AVATAR_UPLOADED_SUCCESSFULLY = "Avatar uploaded successfully";
    public static final String EES_USER_AVATAR_DELETED_SUCCESSFULLY = "Avatar deleted successfully";
    public static final String EES_USER_INFO_LANGUAGE = "User language changed successfully";
    public static final String EES_USER_INFO_AUTH_2_FACTORY = "User secure authentication changed successfully";
    public static final String EES_USER_VERIFICATION_NOT_FOUND = "Verification user not found with email %s and code %s";
    public static final String EES_USER_CODE_VERIFICATION_OK = "Verification code providing is correct. Continue enjoy your journey to Enexse Platform.";
    public static final String EES_USER_CODE_VERIFICATION_KO = "Your verification code providing isn't valid or expired. Try resend new code.";

    // PERMISSIONS
    public static final String EES_PERMISSION_PREFIX = "EES-PERMISSION-";
    public static final String EES_PERMISSION_CODE_COULD_NOT_BE_NULL = " Permission code could not be null";
    public static final String EES_PERMISSION_CODE_INVALID_FORMAT = "PermissionCode must starts with EES-PERMISSION-";
    public static final String EES_PERMISSION_ALREADY_EXISTS = "Permission already exists with code %s. Try another one ";
    public static final String EES_PERMISSION_CREATED = "Permission created Successfully";
    public static final String EES_PERMISSION_NOT_FOUND = "Permission does not exist with ";
    public static final String EES_PERMISSION_DELETED = "Permission deleted successfully";
    public static final String EES_UPDATE_BY_CODE_PERMISSION = "Permission updated successfully";

    // REQUESTS
    public static final String EES_REQUEST_CREATED = "Request created successfully.";
    public static final String EES_REQUEST_ALREADY_EXISTS = "Request already exists with code %s. Try another one.";
    public static final String EES_REQUEST_DELETED = "Request deleted successfully .";
    public static final String EES_REQUEST_NOT_FOUND = "Request not found with ";
    public static final String EES_UPDATE_BY_CODE_REQUEST = "Request updated successfully.";
    public static final String EES_REQUEST_INVALID_LENGTH = "RequestCode must be EES-DMD-XXX.";
    public static final String EES_REQUEST_PREFIX = "EES-DMD-";

    // SUB-REQUESTS
    public static final String EES_SUB_REQUEST_NOT_FOUND = "Sub-Request not found with ";
    public static final String EES_SUB_REQUEST_ALREADY_EXISTS = "subRequest already exists with code %s. Try another one.";
    public static final String EES_SUB_REQUEST_DELETED = "Sub-Request deleted successfully";
    public static final String EES_SUB_REQUEST_INVALID_LENGTH = "RequestCode must be EES-DMD-XXX-XXX";
    public static final String EES_SUB_REQUEST_CREATED = "subRequest created successfully";
    public static final String EES_UPDATE_BY_ID_SUB_REQUEST = "SubRequest updated successfully.";

    // ENTITIES
    public static final String EES_ENTITY_CREATED = "Entity created successfully.";
    public static final String EES_ENTITY_ALREADY_EXISTS = "Entity already exists with code %s. Try another one.";
    public static final String EES_ENTITY_PREFIX = "EES-ENTITY-";
    public static final String EES_ENTITY_INVALID_LENGTH = "Entity code must be EES-ENTITY-XXX";
    public static final String EES_ENTITY_NOT_FOUND = "Entity not found with code ";
    public static final String EES_ENTITY_DELETED = "Entity deleted successfully";
    public static final String EES_UPDATE_BY_CODE_ENTITY = "Entity updated successfully.";

    // DEPARTMENTS
    public static final String EES_DEPARTMENT_CREATED = "Department created successfully.";
    public static final String EES_DEPARTMENT_ALREADY_EXISTS = "Department already exists with code %s. Try another one.";
    public static final String EES_DEPARTMENT_PREFIX = "EES-DEPARTMENT-";
    public static final String EES_DEPARTMENT_INVALID_LENGTH = "DepartmentCode must be EES-DEPARTMENT-XXX";
    public static final String EES_DEPARTMENT_NOT_FOUND = "Department Not Found with ";
    public static final String EES_DEPARTMENT_DELETED = "Department deleted successfully";
    public static final String EES_UPDATE_BY_CODE_DEPARTMENT = "Department updated successfully.";

    // CUSTOMERS
    public static final String EES_CUSTOMER_PREFIX = "EES-CLIENT-";
    public static final String EES_CUSTOMER_INVALID_LENGTH = "Customer code must be EES-CLIENT-XXX.";
    public static final String EES_CUSTOMER_ALREADY_EXISTS = "Customer already exists with code %s. Try another one.";
    public static final String EES_CUSTOMER_CREATED = "Customer created successfully.";
    public static final String EES_CUSTOMER_NOT_FOUND = "Customer not found with %s";
    public static final String EES_CUSTOMER_UPDATED = "Customer updated successfully.";
    public static final String EES_CUSTOMER_UPDATED_STATUS = "Customer status updated successfully.";
    public static final String EES_CUSTOMER_DELETED = "Customer deleted successfully.";

    // PROFESSIONS
    public static final String EES_PROFESSION_PREFIX = "EES-PROFESSION-";
    public static final String EES_PROFESSION_CODE_COULD_NOT_BE_NULL = "Profession code could not be null";
    public static final String EES_PROFESSION_CODE_INVALID_FORMAT = "Profession code must starts with EES-PROFESSION-";
    public static final String EES_PROFESSION_ALREADY_EXISTS = "Profession already exists with code %s. Try another one ";
    public static final String EES_PROFESSION_CREATED = "Profession created Successfully";
    public static final String EES_PROFESSION_UPDATED = "Profession updated successfully.";
    public static final String EES_PROFESSION_NOT_FOUND = "Profession not found";
    public static final String EES_PROFESSION_DELETED = "Profession deleted successfully";

    // TIMESHEET WORKPLACE
    public static final String EES_TIMESHEET_WORKPLACE_PREFIX = "EES-WORKPLACE-";
    public static final String EES_TIMESHEET_WORKPLACE_CODE_COULD_NOT_BE_NULL = "TimeSheet workspace code could not be null";
    public static final String EES_TIMESHEET_WORKPLACE_CODE_INVALID_FORMAT = "TimeSheet workspace must starts with EES-WORKPLACE-";
    public static final String EES_TIMESHEET_WORKPLACE_ALREADY_EXISTS = "TimeSheet workspace already exists with code %s. Try another one";
    public static final String EES_TIMESHEET_WORKPLACE_CREATED = "TimeSheet workplace created Successfully";
    public static final String EES_TIMESHEET_WORKPLACE_NOT_FOUND = "TimeSheet workplace not found";
    public static final String EES_UPDATE_BY_CODE_TIMESHEET_WORKPLACE = "TimeSheet workplace updated successfully.";
    public static final String EES_TIMESHEET_WORKPLACE_DELETED = "TimeSheet workplace deleted successfully";

    // TIMESHEET ACTIVITY
    public static final String EES_TIMESHEET_ACTIVITY_PREFIX = "EES-ACTIVITY-";
    public static final String EES_TIMESHEET_ACTIVITY_CODE_COULD_NOT_BE_NULL = "TimeSheet activity code could not be null";
    public static final String EES_TIMESHEET_ACTIVITY_CODE_INVALID_FORMAT = "TimeSheet activity must starts with EES-ACTIVITY-";
    public static final String EES_TIMESHEET_ACTIVITY_ALREADY_EXISTS = "TimeSheet activity already exists with code %s. Try another one";
    public static final String EES_TIMESHEET_ACTIVITY_CREATED = "TimeSheet activity created Successfully";
    public static final String EES_TIMESHEET_ACTIVITY_NOT_FOUND = "TimeSheet activity not found";
    public static final String EES_UPDATE_BY_CODE_TIMESHEET_ACTIVITY = "TimeSheet activity updated successfully.";
    public static final String EES_TIMESHEET_ACTIVITY_DELETED = "TimeSheet activity deleted successfully";

    // TIMESHEET CONTRACT HOURS
    public static final String EES_TIMESHEET_CONTRACTHOURS_PREFIX = "EES-CONTRACTHOURS-";
    public static final String EES_TIMESHEET_CONTRACTHOURS_CODE_COULD_NOT_BE_NULL = "TimeSheet contract hours code could not be null";
    public static final String EES_TIMESHEET_CONTRACTHOURS_CODE_INVALID_FORMAT = "TimeSheet activity must starts with EES-CONTRACTHOURS-";
    public static final String EES_TIMESHEET_CONTRACTHOURS_CODE_ALREADY_EXISTS = "TimeSheet contract hours already exists with code %s. Try another one";
    public static final String EES_TIMESHEET_CONTRACTHOURS_ALREADY_EXISTS = "TimeSheet contract hours already exists with hours %s. Try another one";
    public static final String EES_TIMESHEET_CONTRACTHOURS_CREATED = "TimeSheet contract hours created Successfully";
    public static final String EES_TIMESHEET_CONTRACTHOURS_NOT_FOUND = "TimeSheet contract hours not found";
    public static final String EES_UPDATE_BY_CODE_TIMESHEET_CONTRACTHOURS = "TimeSheet contract hours updated successfully.";
    public static final String EES_TIMESHEET_CONTRACTHOURS_DELETED = "TimeSheet contract hours deleted successfully";
    public static final String EES_TIMESHEET_CONTRACTHOURS_NOTHING_TO_UPDATE = "Nothing to update";

    // LINK
    public static final String EES_LINK_NOT_FOUND = "Link not found with";
    public static final String EES_USER_ERROR_SEND_LINK = "We have some problems sending link to";
    public static final String EES_LINK_CREATED = "link created successfully.";

    // CREATE REQUEST
    public static final String EES_CREATE_REQUEST_CREATED = "Request created successfully";
    public static final String EES_REQUEST_ATTACH_FILES = "files attached exceeds the size limits(3MB)";
    public static final String EES_REQUEST_STATUS_UPDATED = "Request status updated ";

    // ERRORS USER
    public static final String EES_USER_UPDATE_PROFILE_NOT_FOUND = "User Profile %s can't be updated.";
    public static final String EES_USER_DELETE_ERROR = "Something wrong bad. User hasn't deleted successfully.";
    public static final String EES_ORGANIZATION_DELETE_ERROR = "Something wrong bad. Organization hasn't deleted successfully.";
    public static final String EES_USER_GENERIC_ERROR = "Generic Error. Something wrong/happen bad on the system. Please, try again later.";
    public static final String EES_USER_ERROR_LOGIN = "Bad Credentials Username/Email/UserID and (or) Password incorrect(s).";
    public static final String EES_USER_ERROR_NOT_FOUND_WITH_ID = "User not found with userId ";
    public static final String EES_ORGANIZATION_ERROR_NOT_FOUND_WITH_ID = "Organization not found with organizationId ";
    public static final String EES_OBJECT_ERROR_NOT_FOUND = "Object not found with property ";
    public static final String EES_USER_ERROR_NOT_FOUND_WITH_NAME = "User not found with username/email/userID ";
    public static final String EES_USER_ERROR_VERIFICATION_EMAIL = "We have some problems for the verification email ";
    public static final String EES_USER_ERROR_SEND_EMAIL = "We have some problems sending email to ";
    public static final String EES_ERROR_ALREADY_USED = " is already used, please try another one";
    public static final String EES_USER_ERROR_PASSWORD = "The previous old user password isn't correct. Try again !!";
    public static final String EES_USER_ERROR_PASSWORD_NOT_MATCH = "User old password isn't correct. Try again !!";
    public static final String EES_USER_ERROR_PASSWORD_OPERATION = "The password provided to perform the operation isn't correct. Try again !!";
    public static final String EES_USER_ERROR_DISABLE = "Your account isn't active. Contact your administration department for more information.";
    public static final String EES_USER_NOT_PRESENT = "We don't have any user registered yet.";
    public static final String EES_USER_ERROR_TOKEN = "Your token is expired or not valid. Please try login again.";
    public static final String EES_USER_ERROR_REFRESH_TOKEN = "The refresh token that you sending isn't not present in database. Try again";
    public static final String EES_USER_ERROR_NOT_FOUND_VERIFY_LINK = "Link and verify type that you are provided aren't corrects. Try resend the new link to complete the operation";
    public static final String EES_USER_ERROR_WRONG_ATTACHMENT = "Could not save this attachment ";
    public static final String EES_USER_ERROR_SAVE_ATTACHMENT = " File contains wrong characters. Try another one.";
    public static final String EES_USER_ERROR_NOT_FOUND_ATTACHMENT = "User file not found with userID: ";
    public static final String EES_USER_ERROR_VIEW_PDF = "error on view pdf";
    public static final String EES_USER_ERROR_CODE = "The code provided to verify your request isn't correct. Try another code.";

    // CONTRACTS
    public static final String EES_CONTRACT_PREFIX = "EES-CONTRACT-";
    public static final String EES_CONTRACT_INVALID_LENGTH = "Contract code must be EES-CONTRACT-XXX.";
    public static final String EES_CONTRACT_ALREADY_EXISTS = "Contract already exists with code %s. Try another one.";
    public static final String EES_CONTRACT_CREATED = "Contract created successfully.";
    public static final String EES_CONTRACT_NOT_FOUND = "Contract not found with %s";
    public static final String EES_CONTRACT_UPDATED = "Contract updated successfully.";
    public static final String EES_CONTRACT_UPDATED_STATUS = "Contract status updated successfully.";
    public static final String EES_CONTRACT_DELETED = "Contract deleted successfully.";

    // ADMINISTRATION MESSAGE
    public static final String EES_MESSAGE_CREATED = "Message sent Successfully";
    public static final String EES_MESSAGE_TYPE_PREFIX = "EES-MESSAGE-TYPE-";
    public static final String EES_MESSAGE_TYPE_CREATED = "Message type created successfully.";
    public static final String EES_MESSAGE_TYPE_INVALID_LENGTH = "Message type code must be EES-MESSAGE-TYPE-XXX";
    public static final String EES_MESSAGE_TYPE_ALREADY_EXISTS = "Message type already exists with code %s. Try another one.";
    public static final String EES_MESSAGE_TYPE_NOT_FOUND = "Message type not found with code ";
    public static final String EES_MESSAGE_TYPE_UPDATED = "Message type updated successfully";
    public static final String EES_MESSAGE_TYPE_DELETED = "Message type deleted successfully";

    // ALERT MESSAGE
    public static final String EES_AlERT_CREATED = "Alert Message created Successfully";
    public static final String EES_ALERT_NOT_FOUND = "Alert not found with %s";
    public static final String EES_ALERT_UPDATED = "Alert message updated successfully";
    public static final String EES_ALERT_DELETED = "Alert message deleted successfully";

    // NEWS
    public static final String EES_NEWS_CREATED = " The new created Successfully";
    public static final String EES_NEWS_NOT_FOUND = "the new not found with ";
    public static final String EES_NEWS_UPDATED = "the new updated successfully";
    public static final String EES_NEWS_DELETED = "the new deleted successfully";

    // CLOUDINARY
    public static final String EES_CLOUDINARY_UPLOADED = "Document/file uploaded successfully.";
    public static final String EES_CLOUDINARY_DELETED = "Document/file deleted successfully.";
    public static final String EES_CLOUDINARY_FOUND = "Document/file found successfully.";
    public static final String EES_CLOUDINARY_DOC_NOT_FOUND = "Document/file not found with %s .";

    // CASE NUMBER
    public static final String EES_CASE_NUMBER_INVALID_LENGTH = "invalid length";
    public static final String EES_CASE_NUMBER_ALREADY_EXISTS = " case number already exists";
    public static final String EES_CASE_NUMBER_CREATED = "case number created successfully";
    public static final String EES_CASE_NUMBER_NOT_FOUND = "case number not found";
    public static final String EES_CASE_NUMBER_UPDATED = "case number updated successfully";
    public static final String EES_CASE_NUMBER_DELETED = "case number deleted successfully";

    // ORDER NUMBER
    public static final String EES_ORDER_NUMBER_ALREADY_EXISTS = " order number already exists";
    public static final String EES_ORDER_NUMBER_CREATED = "order number created successfully";
    public static final String EES_ORDER_NUMBER_NOT_FOUND = "order number not found";
    public static final String EES_ORDER_NUMBER_UPDATED = "order number updated successfully";
    public static final String EES_ORDER_NUMBER_DELETED = "order number deleted successfully";
}
