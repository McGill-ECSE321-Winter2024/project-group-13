import _ from "lodash";
import { CourseState } from "../../helpers/enums";

export default function CourseStatusBadge ({
    status
}: {
    status: CourseState
})  {
    const className = {
        [CourseState.Approved]: 'inline-flex items-center rounded-md bg-green-500/10 px-2 py-1 text-xs font-medium text-green-400 ring-1 ring-inset ring-green-500/20',
        [CourseState.AwaitingApproval]: 'inline-flex items-center rounded-md bg-yellow-400/10 px-2 py-1 text-xs font-medium text-yellow-500 ring-1 ring-inset ring-yellow-400/20',
        [CourseState.Denied]: 'inline-flex items-center rounded-md bg-red-400/10 px-2 py-1 text-xs font-medium text-red-400 ring-1 ring-inset ring-red-400/20',
        [CourseState.Inactive]: 'inline-flex items-center rounded-md bg-green-500/10 px-2 py-1 text-xs font-medium text-green-400 ring-1 ring-inset ring-green-500/20',
    };
    return <span className={className[status]}>
    {_.capitalize(status)}
  </span>
}