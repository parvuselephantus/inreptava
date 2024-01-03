import ClassModel from "../API/ClassModel";
import Field from "../API/Field";
import SetMethodRequest from "../API/request/SetMethodRequest";
import { useClassModifications } from "../hooks/useClassModifications";
import { useClasses } from "../hooks/useClasses";

function ClassList() {
    let {mutate, isPending, isError} = useClassModifications();
    let {classes, isFetching} = useClasses();
    if (isFetching) {
        return <>Loading...</>
    }

    const onGetSetSwitch = (c: ClassModel, method: string, add: boolean, field: Field) => {
        let req: SetMethodRequest = {
            add,
            fullClassName: c.fullClassName,
            method,
            field
        }
        mutate(req);
    }

    return <>{classes.map((c: ClassModel, classNo: number) => 
        <div key={"classNo" + classNo}>
            {c.name}
            <ul>
                {c.fields.map((f, i) => <li key={"field_" + classNo + "_" + i}>
                    {f.type} {f.name}
                    <input type="checkbox" defaultChecked={f.getter} onChange={e => onGetSetSwitch(c, "get", e.target.checked, f)} id={"getter_" + classNo + "_" + i} name={"getter_" + classNo + "_" + i}/><label htmlFor={"getter_" + classNo + "_" + i}>Getter</label>
                    <input type="checkbox" defaultChecked={f.setter} onChange={e => onGetSetSwitch(c, "set", e.target.checked, f)} id={"setter_" + classNo + "_" + i} name={"setter_" + classNo + "_" + i}/><label htmlFor={"setter_" + classNo + "_" + i}>Setter</label>
                </li>)}
            </ul>
        </div>
    )}</>
}

export default ClassList;