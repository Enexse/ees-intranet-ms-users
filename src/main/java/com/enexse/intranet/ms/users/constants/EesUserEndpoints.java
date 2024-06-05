package com.enexse.intranet.ms.users.constants;

public class EesUserEndpoints {

    // ROOT
    public static final String EES_ROOT_ENDPOINT = "/user-service/api/v1";
    public static final String EES_ACCOUNTING_ENDPOINT = "/accounting-service/api/v1";

    // AUTH
    public static final String EES_AUTH_USER = "/auth/login";
    public static final String EES_VERIFY_LINK_USER = "/auth/verify/link";

    // USERS
    public static final String EES_CREATE_USER = "/user/create";
    public static final String EES_LOGIN_USER = "/user/login";
    public static final String EES_UPDATE_AVATAR = "/user/avatar/{userId}";
    public static final String EES_DELETE_AVATAR = "/user/avatar/delete/{userId}";
    public static final String EES_USER_ASSIGN_ROLE = "/user/assign-role/{userId}";
    public static final String EES_INSERT_USER = "/user/insert";
    public static final String EES_UPDATE_PROFIL_USER = "/user/updateProfil/{userId}";
    public static final String EES_UPDATE_COLLABORATOR_INFO = "/user/updateCollaboratorInfo/{userId}";
    public static final String EES_UPDATE_COLLABORATOR_STATUS = "/user/updateCollaboratorStatus/{userId}";
    public static final String EES_GET_DEPARTMENT = "/user/getDepartment/{userId}";
    public static final String EES_GET_ROLE = "/user/getRole/{userId}";
    public static final String EES_GET_ENTITY = "/user/getEntity/{userId}";
    public static final String EES_GET_PROFESSION = "/user/getProfession/{userId}";
    public static final String EES_GET_LAST_USERS = "/user/getLastUsers";
    public static final String EES_GET_ALL_USERS_PAGINATED = "/user/getAllPaginated";
    public static final String EES_GET_ALL_USERS_FILTRED = "/user/getAllFiltred";
    public static final String EES_GET_ALL_USERS = "/user/getAll";
    public static final String EES_UPDATE_USER = "/user/update/{userId}";
    public static final String EES_ARCHIVE_USER = "/user/archive/{userId}";
    public static final String EES_UPDATE_TIMESHEET_USER = "/user/update/timesheet/{userId}";
    public static final String EES_EMAIL_USER = "/user/email";
    public static final String EES_CERTIFICATE_EMAIL_USER = "/user/certificate/email";
    public static final String EES_SEND_CODE_TWO_FACTORY_AUTH = "/user/twoFactory/authentication";
    public static final String EES_GET_USER_BY_ID = "/user/getByCode/{userId}";
    public static final String EES_GET_USER_BY_ENEXSE_EMAIL = "/user/getByEnexseEmail";
    public static final String EES_UPDATE_PASSWORD = "/user/updatepassword/{userId}";
    public static final String EES_UPDATE_LAST_LOGIN = "/user/updateLastLogin/{userId}";
    public static final String EES_CHANGE_PASSWORD = "/user/changepassword/{userId}";
    public static final String EES_CHANGE_PERSONAL_EMAIL = "/user/changePersonalEmail/{userId}";
    public static final String EES_ACTIVATE_OR_DISABLED = "/user/certification";
    public static final String EES_USER_IS_TEMPORARY_PASSWORD = "/user/isTemporaryPassword";
    public static final String EES_USER_IS_PASSWORD_EXPIRED = "/user/isPasswordExpired/{userId}";
    public static final String EES_ACTIVATE_USER = "/user/activateUser";
    public static final String EES_DISABLED_USER = "/user/disableUser";
    public static final String EES_UPDATE_USER_INFO_LANGUAGE = "/user/update/language";
    public static final String EES_UPDATE_USER_INFO_2FACTORY_AUTH = "/user/update/auth2Factory";
    public static final String EES_VERIFY_CODE_AUTH = "/user/verify/authCode";
    public static final String EES_RESEND_INVITATION_USER = "/user/resendInvitation";

    // USER CAREERS
    public static final String EES_ADD_MISSION_TO_COLLABORATOR = "/user/addMission/{userId}";
    public static final String EES_DELETE_MISSION_FROM_COLLABORATOR = "/user/deleteMission/{userId}";
    public static final String EES_DELETE_FORMATION_FROM_COLLABORATOR = "/user/deleteFormation/{userId}";

    // USER FORMATIONS
    public static final String EES_GET_MISSIONS_FROM_COLLABORTOR = "/user/getMissions/{userId}";
    public static final String EES_GET_FORMATIONS_FROM_COLLABORTOR = "/user/getFormations/{userId}";
    public static final String EES_UPDATE_MISSION_COLLABORATOR = "/user/updateMission/{userId}";
    public static final String EES_ADD_COMMENT_MISSION_COLLABORATOR = "/user/addCommentMission/{userId}";
    public static final String EES_UPDATE_FORMATION_COLLABORATOR = "/user/updateFormation/{userId}";
    public static final String EES_ADD_FORMATION_TO_COLLABORATOR = "/user/addFormation/{userId}";

    // GROUPS
    public static final String EES_INSERT_GROUP = "/group/insert";
    public static final String EES_GET_ALL_GROUPS = "/group/getAll";
    public static final String EES_DELETE_GROUP = "/group/deleteByCode/{groupCode}";
    public static final String EES_GET_GROUP_BY_CODE = "/group/getByCode/{groupCode}";
    public static final String EES_UPDATE_GROUP_BY_CODE = "/group/updateByCode/{groupCode}";
    public static final String EES_SEND_MAIL_TO_GROUP = "/group/sendMail";
    public static final String EES_SEND_MAIL_TO_COLLABORATOR = "/collaborator/sendMail";

    // ROLES
    public static final String EES_INSERT_ROLE = "/role/insert";
    public static final String EES_GET_All_ROLES = "/role/getAllRoles";
    public static final String EES_GET_All_KEYCLOACK_ROLES = "/keycloack/getRoles";
    public static final String EES_GET_ROLE_BY_CODE_ROLE = "/role/getByCode";
    public static final String EES_UPDATE_BY_CODE_ROLE = "/role/updateByCode/{roleCode}";
    public static final String EES_DELETE_BY_CODE_ROLE = "/role/deleteByCode/{roleCode}";
    public static final String EES_GET_PERMISSIONS_BY_CODE_ROLE = "/role/permissions/{roleCode}";
    public static final String EES_GET_ROLE_BY_TITLE = "/role/getByTitle";
    public static final String EES_GET_USERS_BY_ROLE = "/role/getUsers/{roleCode}";

    // REQUESTS
    public static final String EES_INSERT_REQUEST = "/request/insert";
    public static final String EES_GET_REQUEST_BY_ID = "/request/getById/{requestId}";
    public static final String EES_GET_All_REQUESTS = "/request/getAllRequests";
    public static final String EES_GET_REQUEST_BY_CODE_REQUEST = "/request/getByCode";
    public static final String EES_UPDATE_BY_CODE_REQUEST = "/request/updateByCode/{requestCode}";
    public static final String EES_DELETE_BY_CODE_REQUEST = "/request/deleteByCode/{requestCode}";

    // SUB-REQUEST
    public static final String EES_INSERT_SUB_REQUEST = "/subrequest/insert";
    public static final String EES_GET_All_SUB_REQUESTS = "/subrequest/getAllSubRequests";
    public static final String EES_GET_SUB_REQUEST_BY_REQUEST_CODE = "/subrequest/getByRequestCode/{requestCode}";
    public static final String EES_GET_SUB_REQUEST_BY_ID = "/subrequest/getById/{subRequestId}";
    public static final String EES_UPDATE_BY_CODE_SUB_REQUEST = "/subrequest/updateByCode/{subrequestCode}";
    public static final String EES_DELETE_BY_CODE_SUB_REQUEST = "/subrequest/deleteByCode/{subrequestCode}";
    public static final String EES_GET_LIST_OF_SUB_REQUESTS_BY_CODE = "/subrequest/getAllSubRequestsByCode";

    public static final String EES_GET_LIST_OF_SUB_REQUESTS_BY_ID = "/subrequest/getAllSubRequestsById";

    // ENTITIES
    public static final String EES_INSERT_ENTITY = "/entity/insert";
    public static final String EES_GET_All_ENTITIES = "/entity/getAllEntities";
    public static final String EES_GET_ENTITY_BY_CODE_ENTITY = "/entity/getByCode/{entityCode}";
    public static final String EES_UPDATE_BY_CODE_ENTITY = "/entity/updateByCode/{entityCode}";
    public static final String EES_DELETE_BY_CODE_ENTITY = "/entity/deleteByCode/{entityCode}";
    public static final String EES_GET_USERS_BY_ENTITY = "/entity/getUsers/{entityCode}";

    // DEPARTMENTS
    public static final String EES_INSERT_DEPARTMENT = "/department/insert";
    public static final String EES_GET_All_DEPARTMENTS = "/department/getAllDepartments";
    public static final String EES_GET_DEPARTMENT_BY_CODE_DEPARTMENT = "/department/getByCode/{departmentCode}";
    public static final String EES_UPDATE_BY_CODE_DEPARTMENT = "/department/updateByCode/{departmentCode}";
    public static final String EES_DELETE_BY_CODE_DEPARTMENT = "/department/deleteByCode/{departmentCode}";
    public static final String EES_GET_USERS_BY_DEPARTMENT = "/department/getUsers/{departmentCode}";

    // PERMISSIONS
    public static final String EES_INSERT_PERMISSION = "/permission/insert";
    public static final String EES_GET_All_PERMISSIONS = "/permission/getAllPermissions";
    public static final String EES_GET_PERMISSION_BY_CODE = "/permission/getPermissionByCode/{permissionCode}";
    public static final String EES_DELETE_BY_CODE_PERMISSION = "/permission/deleteByCode/{permissionCode}";
    public static final String EES_UPDATE_BY_CODE_PERMISSION = "/permission/updateByCode/{permissionCode}";
    public static final String EES_GET_PERMISSION_TITLE = "/permission/getPermissionTitle";

    // LINK FORGOT PASSWORD
    public static final String EES_GET_LINK = "/link/getByLink";
    public static final String EES_UPDATE_EXPIRY_DATE_LINK = "/link/updateExpiryDate/{link}";
    public static final String EES_VERIFY_LINK_EXISTS = "/link/existsBylink";

    // CUSTOMERS
    public static final String EES_INSERT_CUSTOMER = "/customer/insert";
    public static final String EES_GET_All_CUSTOMERS = "/customer/getAllCustomers";
    public static final String EES_GET_CUSTOMER_BY_CODE = "/customer/getByCode/{customerCode}";
    public static final String EES_GET_CUSTOMER_BY_ID = "/customer/getById/{customerId}";
    public static final String EES_UPDATE_CUSTOMER_BY_CODE = "/customer/updateByCode/{customerCode}";
    public static final String EES_ACTIVATE_DISABLED_CUSTOMER_BY_CODE = "/customer/activateDisableByCode/{customerCode}";
    public static final String EES_DELETE_CUSTOMER_BY_CODE = "/customer/deleteByCode/{customerCode}";

    // PROFESSIONS
    public static final String EES_INSERT_PROFESSION = "/profession/insert";
    public static final String EES_UPDATE_BY_CODE_PROFESSION = "/profession/updateByCode/{professionCode}";
    public static final String EES_DELETE_BY_CODE_PROFESSION = "/profession/deleteByCode/{professionCode}";
    public static final String EES_GET_PROFESSION_BY_CODE = "/profession/getProfessionByCode/{professionCode}";
    public static final String EES_GET_All_PROFESSIONS = "/profession/getAllProfessions";
    public static final String EES_GET_PROFESSION_BY__DEPARTMENT = "/profession/getByDepartment/{departmentCode}";

    // CREATE REQUEST
    public static final String EES_CREATE_REQUEST = "/create/request";
    public static final String EES_GET_REQUEST_ATTACHMENT = "/downloadFile/{fileId}";
    public static final String EES_GET_LIST_OF_REQUESTS = "/list-of-requests";
    public static final String EES_GET_LIST_RECIPIENTS = "/recipients";
    public static final String EES_GET_LIST_OF_REFERENTS = "/referents";
    public static final String EES_UPDATE_REQUEST = "/update/request/{id}";
    public static final String EES_UPDATE_STATUS_REQUEST = "/update/requestAutomatic/{id}";
    public static final String EES_GET_REQUEST = "/get/request/{id}";
    public static final String EES_GET_LAST_REQUESTS = "/get/lastRequests";
    public static final String EES_GET_REQUESTS_BY_USER = "/get/requests/{userId}";

    //public static final String EES_GET_LIST_OF_SUB_REQUESTS_BY_TITLE = "/list-of-subrequests-by-title/{requestTitle}";

    // TIMESHEET WORKPLACE
    public static final String EES_UPDATE_BY_CODE_TIMESHEET_WORKPLACE = "/workplace/updateByCode/{workPlaceCode}";
    public static final String EES_INSERT_TIMESHEET_WORKPLACE = "/workplace/insert";
    public static final String EES_GET_All_TIMESHEET_WORKPLACES = "/workplace/getAllWorkPlaces";
    public static final String EES_GET_TIMESHEET_WORKPLACE_BY_CODE = "/workplace/getWorkPlaceByCode/{workPlaceCode}";
    public static final String EES_DELETE_BY_CODE_TIMESHEET_WORKPLACE = "/workplace/deleteByCode/{workPlaceCode}";

    // TIMESHEET ACTIVITY
    public static final String EES_UPDATE_BY_CODE_TIMESHEET_ACTIVITY = "/activity/updateByCode/{activityCode}";
    public static final String EES_INSERT_TIMESHEET_ACTIVITY = "/activity/insert";
    public static final String EES_GET_All_TIMESHEET_ACTIVITIES = "/activity/getAllActivities";
    public static final String EES_GET_TIMESHEET_ACTIVITY_BY_CODE = "/activity/getActivityByCode/{activityCode}";
    public static final String EES_DELETE_BY_CODE_TIMESHEET_ACTIVITY = "/activity/deleteByCode/{activityCode}";

    // TIMESHEET CONTRACT HOURS
    public static final String EES_UPDATE_BY_CODE_TIMESHEET_CONTRACTHOURS = "/contractHours/updateByCode/{contractHoursCode}";
    public static final String EES_INSERT_TIMESHEET_CONTRACTHOURS = "/contractHours/insert";
    public static final String EES_GET_All_TIMESHEET_CONTRACTHOURS = "/contractHours/getAllContractHours";
    public static final String EES_GET_TIMESHEET_CONTRACTHOURS_BY_CODE = "/contractHours/getByCode/{contractHoursCode}";
    public static final String EES_DELETE_BY_CODE_TIMESHEET_CONTRACTHOURS = "/contractHours/deleteByCode/{contractHoursCode}";

    // CONTRACTS
    public static final String EES_INSERT_CONTRACT = "/contract/insert";
    public static final String EES_GET_All_CONTRACTS = "/contract/getAllContracts";
    public static final String EES_GET_CONTRACT_BY_CODE = "/contract/getByCode/{contractCode}";
    public static final String EES_UPDATE_CONTRACT_BY_CODE = "/contract/updateByCode/{contractCode}";
    public static final String EES_ACTIVATE_DISABLED_CONTRACT_BY_CODE = "/contract/activateDisableByCode/{contractCode}";
    public static final String EES_DELETE_CONTRACT_BY_CODE = "/contract/deleteByCode/{contractCode}";

    // MESSAGES
    public static final String EES_INSERT_MESSAGE = "/message/insert";
    public static final String EES_GET_All_MESSAGES = "/message/getAllMessages";

    // MESSAGES TYPES
    public static final String EES_INSERT_MESSAGE_TYPE = "/message/type/insert";
    public static final String EES_UPDATE_MESSAGE_TYPE = "/message/type/update/{code}";
    public static final String EES_DELETE_MESSAGE_TYPE = "/message/type/delete/{code}";
    public static final String EES_GET_All_MESSAGE_TYPES = "/message/getAllMessageTypes";
    public static final String EES_ACTIVATE_MESSAGE_TYPE_BY_CODE = "/message/type/activateDisableByCode/{code}";

    // ALERT MESSAGE
    public static final String EES_ALERT_MESSAGE = "/alert/insert";
    public static final String EES_GET_All_ALERTS = "/alert/getAll";
    public static final String EES_GET_ALERT = "/get/alert/{id}";
    public static final String EES_CHANGE_STATUS_ALERT = "/alert/changeStatus/{id}";
    public static final String EES_UPDATE_ALERT = "/alert/update/{id}";
    public static final String EES_DELETE_ALERT = "/alert/delete/{id}";

    // NEWS
    public static final String EES_POST_NEWS = "news/insert";
    public static final String EES_GET_All_NEWS = "/news/getAll";
    public static final String EES_GET_NEWS = "/get/news/{id}";
    public static final String EES_CHANGE_STATUS = "/news/changeStatus/{id}";
    public static final String EES_UPDATE_NEWS = "/news/update/{id}";
    public static final String EES_DELETE_BY_ID = "/news/delete/{id}";

    // CLOUNDINARY
    public static final String EES_CLOUDINARY_INSERT_FILE = "/file/insert";
    public static final String EES_CLOUDINARY_ATTACHMENT_FILES = "/file/attachments/{userId}";
    public static final String EES_CLOUDINARY_GET_ALL_FILES = "/files/getAll";
    public static final String EES_CLOUDINARY_GET_ALL_FILES_USER = "/files/getAllByUserId/{userId}";
    public static final String EES_CLOUDINARY_GET_ALL_FILES_USER_UPLOAD_TYPE = "/files/getAllByUserIdAndUploadType/{userId}";
    public static final String EES_CLOUDINARY_DELETE_FILE_BY_ID = "/file/deleteById";
    public static final String EES_CLOUDINARY_DELETE_FILE_BY_PUBLIC_ID = "/file/deleteByPublicId";
    public static final String EES_CLOUDINARY_FIND_FILE = "/file";
    public static final String EES_CLOUDINARY_FIND_FILE_BY_ID_USERID = "/file/get/{id}/{userId}";

    // CASE NUMBER
    public static final String EES_INSERT_CASE_NUMBER = "/caseNumber/insert";
    public static final String EES_GET_All_CASE_NUMBERS = "/caseNumber/getAll";
    public static final String EES_GET_CASE_NUMBER_BY_CODE = "/caseNumber/getByCode/{code}";
    public static final String EES_UPDATE_CASE_NUMBER_BY_CODE = "/caseNumber/update/{code}";
    public static final String EES_DELETE_CASE_NUMBER_BY_CODE = "/caseNumber/delete/{code}";

    // ORDER NUMBER
    public static final String EES_INSERT_ORDER_NUMBER = "/orderNumber/insert";
    public static final String EES_GET_All_ORDER_NUMBERS = "/orderNumber/getAll";
    public static final String EES_GET_ORDER_NUMBER_BY_CODE = "/orderNumber/getByCode/{code}";
    public static final String EES_UPDATE_ORDER_NUMBER_BY_CODE = "/orderNumber/update/{code}";
    public static final String EES_DELETE_ORDER_NUMBER_BY_CODE = "/orderNumber/delete/{code}";
}
