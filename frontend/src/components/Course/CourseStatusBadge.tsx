// CourseStatusBadge.tsx
import {CourseState} from "../../helpers/enums";
import _ from "lodash";

export default function CourseStatusBadge ({
                                               status,
                                               onClick
                                           }: {
    status: CourseState,
    onClick?: (e) => void
})  {
    const className = {
        [CourseState.Approved]: 'inline-flex items-center rounded-md bg-green-500/10 px-2 py-1 text-xs font-medium text-green-400 ring-1 ring-inset ring-green-500/20',
        [CourseState.AwaitingApproval]: 'inline-flex items-center rounded-md bg-yellow-400/10 px-2 py-1 text-xs font-medium text-yellow-500 ring-1 ring-inset ring-yellow-400/20',
        [CourseState.Denied]: 'inline-flex items-center rounded-md bg-red-400/10 px-2 py-1 text-xs font-medium text-red-400 ring-1 ring-inset ring-red-400/20',
        [CourseState.Inactive]: 'inline-flex items-center rounded-md bg-grey-500/10 px-2 py-1 text-xs font-medium text-grey-400 ring-1 ring-inset ring-grey-500/20',
    };
    return (
        <span className={className[status]} onClick={onClick}>
            {_.capitalize(status)}
        </span>
    );
}
