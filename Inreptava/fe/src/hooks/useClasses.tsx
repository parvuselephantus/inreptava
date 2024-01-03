import { useQuery } from "@tanstack/react-query";
import axiosClient from "../axios";
import GetClassesResponse from "../API/response/GetClassesResponse";

export function useClasses() {
    const { data, isLoading, isError } = useQuery<GetClassesResponse>({
        queryKey: ['CLASSES'],
        staleTime: Infinity,
        queryFn: () => axiosClient.get('/classes').then((res) => res.data),
        enabled: true,
    });
  
    return {
        classes: (data == undefined ? [] : data.classes) ?? [],
        isFetching: isLoading,
        isError: isError
    }
}

