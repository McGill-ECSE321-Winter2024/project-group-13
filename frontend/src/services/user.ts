export default function User(): {
    personId: string;
    personType: string;
    personName: string;
    personEmail: string;
    personPhoneNumber: string;
    personSportCenterId: string;
} {
    const userStore = localStorage.getItem('user');
    if (userStore) {
        return JSON.parse(userStore);
    }
    return {
        personId: '',
        personType: '',
        personName: '',
        personEmail: '',
        personPhoneNumber: '',
        personSportCenterId: ''
    };
}