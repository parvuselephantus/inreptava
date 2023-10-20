import ClassModel from "../API/ClassModel";
import { useClasses } from "../hooks/useClasses";

function ClassList() {
    let {classes, isFetching} = useClasses();
    if (isFetching) {
        return <>Loading...</>
    }
    return <>{classes.map((c: ClassModel) => <div>{c.name}</div>)}</>
}

export default ClassList;