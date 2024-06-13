import loginSpec from "./specs/login.spec";
import registerSpec from "./specs/register.spec";
import sessionsSpec from "./specs/sessions.spec";
import sessionDetailSpec from "./specs/session-detail.spec";
import meSpec from "./specs/me.spec";
import notFoundSpec from "./specs/not-found.spec";
import sessionFormSpec from "./specs/session-form.spec";

loginSpec();
registerSpec();
sessionsSpec();
sessionDetailSpec();
sessionFormSpec();
meSpec();
notFoundSpec();